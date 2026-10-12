package com.gaspoweredcake.client33;

import com.gaspoweredcake.client33.utils.files.AtomicFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/** Real filesystem failure scenarios: the previously saved configuration must survive. */
public final class PersistenceRegression {
    public static void run() throws IOException {
        Path root = Files.createTempDirectory("client33-persistence-");
        try {
            Path target = root.resolve("nested/settings.nbt");
            AtomicFile.write(target, stage -> Files.writeString(stage, "first"));
            check(Files.readString(target).equals("first"), "First save creates missing parent directories");
            AtomicFile.write(target, stage -> {
                check(stage.getParent().equals(target.getParent()), "Staging must share the destination filesystem");
                Files.writeString(stage, "replacement");
            });
            check(Files.readString(target).equals("replacement"), "Complete save replaces old bytes");
            IOException expected = new IOException("Synthetic writer failure");
            try {
                AtomicFile.write(target, stage -> {
                    Files.writeString(stage, "partial");
                    throw expected;
                });
                throw new AssertionError("Failed writer must not report success");
            } catch (IOException failure) {
                check(failure == expected, "Original writer failure must propagate");
            }
            check(Files.readString(target).equals("replacement"), "Partial writer cannot overwrite the previous save");
            Path directory = Files.createDirectory(root.resolve("occupied"));
            Files.writeString(directory.resolve("keep"), "preserved");
            try {
                AtomicFile.write(directory, stage -> Files.writeString(stage, "cannot replace a populated directory"));
                throw new AssertionError("Failed rename must not report success");
            } catch (IOException expectedMoveFailure) {
                check(Files.readString(directory.resolve("keep")).equals("preserved"), "Failed replacement must preserve destination");
            }
            try (var paths = Files.walk(root)) {
                check(paths.noneMatch(path -> path.getFileName().toString().endsWith(".tmp")), "Failed and successful saves clean their staging files");
            }
            System.out.println("Persistence failure regressions passed.");
        } finally {
            try (var paths = Files.walk(root)) {
                for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) Files.delete(path);
            }
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}

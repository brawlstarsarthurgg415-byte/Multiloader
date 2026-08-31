package com.universalmodloader;

import com.universalmodloader.installer.InstallationPlan;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class InstallerLogicTest {

    @Test
    void shouldCreateInstallerPlanWithModDirectory() throws Exception {
        Path tempDir = Files.createTempDirectory("uml-installer-test");

        InstallationPlan plan = InstallationPlan.create(tempDir, "UniversalModLoader");

        assertTrue(Files.exists(plan.getInstallDir()));
        assertTrue(Files.exists(plan.getModsDir()));
        assertEquals("UniversalModLoader", plan.getName());
    }
}

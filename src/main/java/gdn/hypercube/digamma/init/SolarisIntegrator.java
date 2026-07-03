package gdn.hypercube.digamma.init;

import gdn.hypercube.solaris.generator.resource.ResourcePackGenerator;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class SolarisIntegrator implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        ResourcePackGenerator.TARGETS.add("digamma");
    }
}

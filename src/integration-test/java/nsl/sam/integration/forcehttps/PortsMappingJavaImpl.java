package nsl.sam.integration.forcehttps;

import kotlin.Pair;
import nsl.sam.core.annotation.attrtypes.PortsMapping;
import org.jetbrains.annotations.NotNull;

public class PortsMappingJavaImpl implements PortsMapping {

    @NotNull
    @Override
    public Pair<Integer, Integer> getMapping() {
        return new Pair<>(ForceHttpsAndPortsMapFromJavaIT.httpPort, ForceHttpsAndPortsMapFromJavaIT.httpsPort);
    }

}

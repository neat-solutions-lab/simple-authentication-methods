package nsl.sam.exploratory;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;

public class EnableExampleSelector implements ImportSelector {

    private static Logger log = LoggerFactory.getLogger(EnableExampleSelector.class);

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {

        log.info(">>> selectImports() in " + this.getClass().getSimpleName());

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(
                        EnableExample.class.getName(), false));

        String[] values = attributes.getStringArray("values");
        Arrays.stream(values).forEach(it -> {
            System.out.println("->>> " + it);
        });
        return new String[0];
    }
}

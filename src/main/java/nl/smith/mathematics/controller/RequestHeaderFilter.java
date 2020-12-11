package nl.smith.mathematics.controller;

import nl.smith.mathematics.configuration.constant.*;
import nl.smith.mathematics.exception.StringToConstantConfigurationException;
import nl.smith.mathematics.numbertype.RationalNumber;
import nl.smith.mathematics.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This method <description of functionality>
 *
 * @author m.smithhva.nl
 */
public class RequestHeaderFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHeaderFilter.class);

    private final Map<Class<? extends ConstantConfiguration<?>>, Set<String>> configurationOptionsMap;

    private final Map<String, Class<? extends Number>> numberTypesMap;

    public RequestHeaderFilter(Set<Class<? extends Number>> numberTypes) {
        configurationOptionsMap = getConstantConfigurationOptions();
        this.numberTypesMap = numberTypes.stream().collect(Collectors.toMap(Class::getSimpleName, e -> e));
    }

    private static Map<Class<? extends ConstantConfiguration<?>>, Set<String>> getConstantConfigurationOptions() {
        Map<Class<? extends ConstantConfiguration<?>>, Set<String>> constantConfigurationOptions = new HashMap<>();

        constantConfigurationOptions.put(AngleType.class, AngleType.values());
        constantConfigurationOptions.put(RationalNumberNormalize.class, RationalNumberNormalize.values());
        constantConfigurationOptions.put(RationalNumberOutputType.class, RationalNumberOutputType.values());
        constantConfigurationOptions.put(RoundingMode.class, RoundingMode.values());

        return constantConfigurationOptions;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        processConstantConfigurationHeaders(request, response);
        processNumberTypeConfigurationHeader(request, response);
        RationalNumberOutputType.set(RationalNumberOutputType.PredefinedType.EXACT);
        chain.doFilter(request, response);
    }

    private void processConstantConfigurationHeaders(HttpServletRequest request, HttpServletResponse response) {
        for (Class<? extends ConstantConfiguration<?>> configurationClass : configurationOptionsMap.keySet()) {
            String headerName = configurationClass.getSimpleName();
            String headerValue = request.getHeader(headerName);
            if (headerValue != null && !headerValue.isEmpty() && configurationOptionsMap.get(configurationClass).contains(headerValue)) {
                try {
                    ConstantConfiguration.setValue(configurationClass, headerValue);
                } catch (StringToConstantConfigurationException e) {
                    //TODO Add error message.
                    throw new IllegalStateException();
                }
            } else {
                LOGGER.info("\nHeader '{}' in request is not available or has an invalid value.\nLegal values: {}", headerName,
                        configurationOptionsMap.get(configurationClass).stream().collect(Collectors.joining(", ")));
                response.setHeader(headerName, ConstantConfiguration.getValue(configurationClass));
            }

        }
    }

    private void processNumberTypeConfigurationHeader(HttpServletRequest request, HttpServletResponse response) {
        // TODO this is a constant
        String headerName = "numberType";
        String headerValue = request.getHeader(headerName);
        if (headerValue != null && !headerValue.isEmpty() && numberTypesMap.containsKey(headerValue)) {
            ThreadContext.setValue("numberType", numberTypesMap.get(headerValue));
        } else {
            LOGGER.info("\nHeader '{}' in request is not available or has an invalid value.\nLegal values: {}", headerName,
                    numberTypesMap.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.joining(", ")));
            // TODO this is a constant
            ThreadContext.setValue("numberType", RationalNumber.class);
        }
    }


}

package com.vtb.guardian.core.analyzer;

import com.vtb.guardian.core.analyzer.owasp.BolaAnalyzer;
import com.vtb.guardian.core.model.domain.ApiSpecification;
import com.vtb.guardian.core.model.domain.Endpoint;
import com.vtb.guardian.core.model.domain.Vulnerability;
import com.vtb.guardian.core.model.enums.SeverityLevel;
import com.vtb.guardian.core.model.enums.VulnerabilityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for BOLA (Broken Object Level Authorization) Analyzer
 */
class BolaAnalyzerTest {

    private BolaAnalyzer analyzer;
    private ApiSpecification apiSpec;

    @BeforeEach
    void setUp() {
        analyzer = new BolaAnalyzer();
        apiSpec = createTestApiSpecification();
    }

    @Test
    @DisplayName("Should detect BOLA vulnerability when endpoint has ID parameter without authentication")
    void shouldDetectBolaWithoutAuth() {
        // Given
        Endpoint endpoint = Endpoint.builder()
            .path("/users/{id}")
            .method("GET")
            .description("Get user by ID")
            .securitySchemes(List.of()) // No authentication
            .build();
        
        apiSpec.setEndpoints(List.of(endpoint));

        // When
        List<Vulnerability> vulnerabilities = analyzer.analyze(apiSpec);

        // Then
        assertThat(vulnerabilities)
            .isNotEmpty()
            .hasSize(1);
        
        Vulnerability vuln = vulnerabilities.get(0);
        assertThat(vuln.getType()).isEqualTo(VulnerabilityType.BOLA);
        assertThat(vuln.getSeverity()).isEqualTo(SeverityLevel.CRITICAL);
        assertThat(vuln.getEndpoint()).isEqualTo("/users/{id}");
        assertThat(vuln.getMethod()).isEqualTo("GET");
    }

    @Test
    @DisplayName("Should detect HIGH severity BOLA when auth exists but no ownership check")
    void shouldDetectBolaWithAuthButNoOwnership() {
        // Given
        Endpoint endpoint = Endpoint.builder()
            .path("/orders/{orderId}")
            .method("GET")
            .description("Get order details")
            .securitySchemes(List.of("bearerAuth")) // Has authentication
            .build();
        
        apiSpec.setEndpoints(List.of(endpoint));

        // When
        List<Vulnerability> vulnerabilities = analyzer.analyze(apiSpec);

        // Then
        assertThat(vulnerabilities)
            .isNotEmpty();
        
        Vulnerability vuln = vulnerabilities.get(0);
        assertThat(vuln.getSeverity()).isEqualTo(SeverityLevel.HIGH);
    }

    @Test
    @DisplayName("Should NOT detect BOLA when endpoint has no ID parameters")
    void shouldNotDetectBolaWithoutIdParams() {
        // Given
        Endpoint endpoint = Endpoint.builder()
            .path("/users")
            .method("GET")
            .description("List all users")
            .securitySchemes(List.of("bearerAuth"))
            .build();
        
        apiSpec.setEndpoints(List.of(endpoint));

        // When
        List<Vulnerability> vulnerabilities = analyzer.analyze(apiSpec);

        // Then
        assertThat(vulnerabilities).isEmpty();
    }

    @Test
    @DisplayName("Should detect multiple BOLA vulnerabilities in different endpoints")
    void shouldDetectMultipleBolaVulnerabilities() {
        // Given
        List<Endpoint> endpoints = List.of(
            Endpoint.builder()
                .path("/users/{id}")
                .method("GET")
                .securitySchemes(List.of())
                .build(),
            Endpoint.builder()
                .path("/documents/{docId}")
                .method("DELETE")
                .securitySchemes(List.of())
                .build()
        );
        
        apiSpec.setEndpoints(endpoints);

        // When
        List<Vulnerability> vulnerabilities = analyzer.analyze(apiSpec);

        // Then
        assertThat(vulnerabilities).hasSize(2);
    }

    @Test
    @DisplayName("Should include OWASP category in vulnerability")
    void shouldIncludeOwaspCategory() {
        // Given
        Endpoint endpoint = Endpoint.builder()
            .path("/users/{id}")
            .method("GET")
            .securitySchemes(List.of())
            .build();
        
        apiSpec.setEndpoints(List.of(endpoint));

        // When
        List<Vulnerability> vulnerabilities = analyzer.analyze(apiSpec);

        // Then
        assertThat(vulnerabilities.get(0).getOwaspCategory())
            .isEqualTo("API1:2023");
    }

    @Test
    @DisplayName("Should include recommendation in vulnerability")
    void shouldIncludeRecommendation() {
        // Given
        Endpoint endpoint = Endpoint.builder()
            .path("/users/{id}")
            .method("GET")
            .securitySchemes(List.of())
            .build();
        
        apiSpec.setEndpoints(List.of(endpoint));

        // When
        List<Vulnerability> vulnerabilities = analyzer.analyze(apiSpec);

        // Then
        Vulnerability vuln = vulnerabilities.get(0);
        assertThat(vuln.getRecommendation())
            .isNotNull()
            .contains("аутентификацию", "авторизацию");
    }

    @Test
    @DisplayName("Should include PoC exploit in vulnerability")
    void shouldIncludeExploitPoc() {
        // Given
        Endpoint endpoint = Endpoint.builder()
            .path("/users/{id}")
            .method("GET")
            .securitySchemes(List.of())
            .baseUrl("https://api.example.com")
            .build();
        
        apiSpec.setEndpoints(List.of(endpoint));

        // When
        List<Vulnerability> vulnerabilities = analyzer.analyze(apiSpec);

        // Then
        Vulnerability vuln = vulnerabilities.get(0);
        assertThat(vuln.getExploitPoc())
            .isNotNull()
            .contains("curl", "https://api.example.com");
    }

    private ApiSpecification createTestApiSpecification() {
        return ApiSpecification.builder()
            .title("Test API")
            .version("1.0.0")
            .baseUrl("https://api.example.com")
            .endpoints(new ArrayList<>())
            .build();
    }
}


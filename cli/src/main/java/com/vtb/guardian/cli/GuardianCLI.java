package com.vtb.guardian.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * API Security Guardian - Command Line Interface
 * 
 * @author VTB Team
 * @version 1.0.0
 */
@Command(
    name = "guardian",
    description = "API Security Guardian - Automated API Security Analysis",
    version = "1.0.0",
    mixinStandardHelpOptions = true,
    subcommands = {
        GuardianCLI.ScanCommand.class,
        GuardianCLI.ListCommand.class,
        GuardianCLI.ReportCommand.class
    }
)
public class GuardianCLI implements Callable<Integer> {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GuardianCLI()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        System.out.println("API Security Guardian v1.0.0");
        System.out.println("Use --help for available commands");
        return 0;
    }

    /**
     * Scan command - создает новое сканирование
     */
    @Command(
        name = "scan",
        description = "Start a new security scan"
    )
    static class ScanCommand implements Callable<Integer> {

        @Option(
            names = {"-f", "--file"},
            description = "OpenAPI/Swagger specification file"
        )
        private File specFile;

        @Option(
            names = {"-u", "--url"},
            description = "URL to OpenAPI/Swagger specification"
        )
        private String specUrl;

        @Option(
            names = {"-o", "--output"},
            description = "Output file for the report",
            defaultValue = "security-report.html"
        )
        private File outputFile;

        @Option(
            names = {"--format"},
            description = "Report format: HTML, PDF, JSON, SARIF",
            defaultValue = "HTML"
        )
        private String format;

        @Option(
            names = {"--enable-llm"},
            description = "Enable AI-powered analysis",
            defaultValue = "true"
        )
        private boolean enableLlm;

        @Option(
            names = {"--enable-fuzzing"},
            description = "Enable fuzzing tests",
            defaultValue = "false"
        )
        private boolean enableFuzzing;

        @Option(
            names = {"--enable-dynamic"},
            description = "Enable dynamic testing",
            defaultValue = "false"
        )
        private boolean enableDynamic;

        @Option(
            names = {"--fail-on"},
            description = "Fail if vulnerabilities found: CRITICAL, HIGH, MEDIUM, LOW",
            defaultValue = "CRITICAL"
        )
        private String failOn;

        @Option(
            names = {"--api-url"},
            description = "API Guardian server URL",
            defaultValue = "http://localhost:8080/api/v1"
        )
        private String apiUrl;

        @Override
        public Integer call() {
            if (specFile == null && specUrl == null) {
                System.err.println("Error: Either --file or --url must be specified");
                return 1;
            }

            try {
                System.out.println("🔍 Starting security scan...");
                System.out.println();
                
                // Validation
                if (specFile != null && !specFile.exists()) {
                    System.err.println("Error: File not found: " + specFile);
                    return 1;
                }

                // Display configuration
                System.out.println("Configuration:");
                System.out.println("  Specification: " + (specFile != null ? specFile : specUrl));
                System.out.println("  Output: " + outputFile);
                System.out.println("  Format: " + format);
                System.out.println("  AI Analysis: " + (enableLlm ? "✅ Enabled" : "❌ Disabled"));
                System.out.println("  Fuzzing: " + (enableFuzzing ? "✅ Enabled" : "❌ Disabled"));
                System.out.println("  Dynamic Testing: " + (enableDynamic ? "✅ Enabled" : "❌ Disabled"));
                System.out.println("  Fail on: " + failOn);
                System.out.println();

                // Create scan via API
                var scanService = new ScanService(apiUrl);
                String scanId = scanService.createScan(
                    specFile != null ? specFile.getAbsolutePath() : specUrl,
                    enableLlm,
                    enableFuzzing,
                    enableDynamic
                );

                System.out.println("📋 Scan created: " + scanId);
                System.out.println("⏳ Analyzing API security...");
                System.out.println();

                // Wait for completion with progress
                ScanResult result = scanService.waitForCompletion(scanId);

                // Display results
                displayResults(result);

                // Generate report
                System.out.println("📄 Generating report...");
                scanService.downloadReport(scanId, format, outputFile);
                System.out.println("✅ Report saved to: " + outputFile.getAbsolutePath());
                System.out.println();

                // Determine exit code based on findings
                return determineExitCode(result, failOn);

            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
                e.printStackTrace();
                return 1;
            }
        }

        private void displayResults(ScanResult result) {
            System.out.println("🎯 Scan Results:");
            System.out.println("─".repeat(60));
            System.out.println(String.format("  Status: %s", result.getStatus()));
            System.out.println(String.format("  Duration: %s", result.getDuration()));
            System.out.println(String.format("  Total Endpoints: %d", result.getTotalEndpoints()));
            System.out.println();
            System.out.println("📊 Vulnerabilities Found:");
            System.out.println(String.format("  🔴 Critical: %d", result.getCriticalFindings()));
            System.out.println(String.format("  🟠 High: %d", result.getHighFindings()));
            System.out.println(String.format("  🟡 Medium: %d", result.getMediumFindings()));
            System.out.println(String.format("  🟢 Low: %d", result.getLowFindings()));
            System.out.println(String.format("  ℹ️  Info: %d", result.getInfoFindings()));
            System.out.println("─".repeat(60));
            System.out.println();
        }

        private int determineExitCode(ScanResult result, String failOn) {
            return switch (failOn.toUpperCase()) {
                case "CRITICAL" -> result.getCriticalFindings() > 0 ? 1 : 0;
                case "HIGH" -> (result.getCriticalFindings() + result.getHighFindings()) > 0 ? 1 : 0;
                case "MEDIUM" -> (result.getCriticalFindings() + result.getHighFindings() + result.getMediumFindings()) > 0 ? 1 : 0;
                case "LOW" -> result.getTotalFindings() > 0 ? 1 : 0;
                default -> 0;
            };
        }
    }

    /**
     * List command - показывает список сканирований
     */
    @Command(
        name = "list",
        description = "List recent scans"
    )
    static class ListCommand implements Callable<Integer> {

        @Option(
            names = {"--api-url"},
            description = "API Guardian server URL",
            defaultValue = "http://localhost:8080/api/v1"
        )
        private String apiUrl;

        @Option(
            names = {"-n", "--limit"},
            description = "Number of scans to show",
            defaultValue = "10"
        )
        private int limit;

        @Override
        public Integer call() {
            try {
                var scanService = new ScanService(apiUrl);
                var scans = scanService.listScans(limit);

                System.out.println("Recent Scans:");
                System.out.println("─".repeat(100));
                System.out.printf("%-38s %-30s %-12s %-20s%n", 
                    "ID", "Name", "Status", "Created");
                System.out.println("─".repeat(100));

                for (var scan : scans) {
                    System.out.printf("%-38s %-30s %-12s %-20s%n",
                        scan.getId(),
                        truncate(scan.getName(), 30),
                        scan.getStatus(),
                        scan.getCreatedAt()
                    );
                }

                return 0;
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                return 1;
            }
        }

        private String truncate(String str, int length) {
            return str.length() > length ? str.substring(0, length - 3) + "..." : str;
        }
    }

    /**
     * Report command - генерирует отчет для существующего scan
     */
    @Command(
        name = "report",
        description = "Generate report for a scan"
    )
    static class ReportCommand implements Callable<Integer> {

        @Parameters(
            index = "0",
            description = "Scan ID"
        )
        private String scanId;

        @Option(
            names = {"-o", "--output"},
            description = "Output file",
            required = true
        )
        private File outputFile;

        @Option(
            names = {"--format"},
            description = "Report format: HTML, PDF, JSON, SARIF",
            defaultValue = "HTML"
        )
        private String format;

        @Option(
            names = {"--api-url"},
            description = "API Guardian server URL",
            defaultValue = "http://localhost:8080/api/v1"
        )
        private String apiUrl;

        @Override
        public Integer call() {
            try {
                System.out.println("📄 Generating report for scan: " + scanId);
                
                var scanService = new ScanService(apiUrl);
                scanService.downloadReport(scanId, format, outputFile);
                
                System.out.println("✅ Report saved to: " + outputFile.getAbsolutePath());
                return 0;
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }
}


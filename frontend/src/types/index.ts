export type ScanStatus = 'PENDING' | 'RUNNING' | 'COMPLETED' | 'FAILED';

export type SeverityLevel = 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW' | 'INFO';

export type VulnerabilityType =
  | 'BOLA'
  | 'BROKEN_AUTH'
  | 'EXCESSIVE_DATA'
  | 'RATE_LIMITING'
  | 'BROKEN_FUNCTION_AUTH'
  | 'BUSINESS_FLOW'
  | 'SSRF'
  | 'MISCONFIGURATION'
  | 'INVENTORY'
  | 'UNSAFE_CONSUMPTION';

export interface Scan {
  id: string;
  name: string;
  description?: string;
  status: ScanStatus;
  specificationUrl?: string;
  specificationType?: string;
  totalEndpoints?: number;
  criticalFindings: number;
  highFindings: number;
  mediumFindings: number;
  lowFindings: number;
  infoFindings: number;
  durationMs?: number;
  createdAt: string;
  updatedAt: string;
  startedAt?: string;
  completedAt?: string;
  errorMessage?: string;
}

export interface Vulnerability {
  id: string;
  type: VulnerabilityType;
  severity: SeverityLevel;
  title: string;
  description: string;
  endpoint: string;
  method: string;
  recommendation?: string;
  codeExample?: string;
  exploitPoc?: string;
  owaspCategory?: string;
  affectedParameter?: string;
  cvssScore?: number;
  aiGenerated: boolean;
  aiConfidence?: number;
  references: string[];
  detectedAt: string;
}

export interface CreateScanRequest {
  name: string;
  description?: string;
  specificationUrl?: string;
  specificationFile?: File;
  projectId?: string;
  options?: {
    enableLlmAnalysis?: boolean;
    enableDynamicTesting?: boolean;
    enableFuzzing?: boolean;
  };
}

export interface ScanStatistics {
  totalEndpoints: number;
  analyzedEndpoints: number;
  totalVulnerabilities: number;
  vulnerabilitiesBySeverity: Record<SeverityLevel, number>;
  vulnerabilitiesByType: Record<VulnerabilityType, number>;
  aiGeneratedFindings: number;
  averageConfidence: number;
}


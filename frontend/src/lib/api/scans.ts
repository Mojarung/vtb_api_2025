import { apiClient } from './client';
import type { Scan, CreateScanRequest, Vulnerability, ScanStatistics } from '@/types';

export const scansApi = {
  /**
   * Get all scans with optional filters
   */
  getAll: async (params?: {
    projectId?: string;
    status?: string;
    page?: number;
    size?: number;
  }): Promise<{ content: Scan[]; totalElements: number; totalPages: number }> => {
    const { data } = await apiClient.get('/scans', { params });
    return data;
  },

  /**
   * Get scan by ID
   */
  getById: async (id: string): Promise<Scan> => {
    const { data } = await apiClient.get(`/scans/${id}`);
    return data;
  },

  /**
   * Create new scan
   */
  create: async (request: CreateScanRequest): Promise<Scan> => {
    const { data } = await apiClient.post('/scans', request);
    return data;
  },

  /**
   * Delete scan
   */
  delete: async (id: string): Promise<void> => {
    await apiClient.delete(`/scans/${id}`);
  },

  /**
   * Get vulnerabilities for a scan
   */
  getVulnerabilities: async (
    scanId: string,
    filters?: {
      severity?: string[];
      type?: string[];
    }
  ): Promise<Vulnerability[]> => {
    const { data } = await apiClient.get(`/scans/${scanId}/vulnerabilities`, {
      params: filters,
    });
    return data;
  },

  /**
   * Get scan statistics
   */
  getStatistics: async (scanId: string): Promise<ScanStatistics> => {
    const { data } = await apiClient.get(`/scans/${scanId}/statistics`);
    return data;
  },

  /**
   * Export report
   */
  exportReport: async (
    scanId: string,
    format: 'HTML' | 'PDF' | 'JSON' | 'SARIF'
  ): Promise<Blob | object> => {
    const { data } = await apiClient.get(`/reports/${scanId}`, {
      params: { format },
      responseType: format === 'PDF' ? 'blob' : 'json',
    });
    return data;
  },
};


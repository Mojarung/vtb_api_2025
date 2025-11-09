import { type ClassValue, clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function formatDate(date: string | Date): string {
  return new Intl.DateTimeFormat('ru-RU', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }).format(new Date(date));
}

export function formatDuration(ms: number): string {
  const seconds = Math.floor(ms / 1000);
  const minutes = Math.floor(seconds / 60);
  const hours = Math.floor(minutes / 60);

  if (hours > 0) {
    return `${hours}ч ${minutes % 60}м`;
  }
  if (minutes > 0) {
    return `${minutes}м ${seconds % 60}с`;
  }
  return `${seconds}с`;
}

export function getSeverityColor(severity: string): string {
  const colors = {
    CRITICAL: 'text-red-600 bg-red-50',
    HIGH: 'text-orange-600 bg-orange-50',
    MEDIUM: 'text-yellow-600 bg-yellow-50',
    LOW: 'text-green-600 bg-green-50',
    INFO: 'text-gray-600 bg-gray-50',
  };
  return colors[severity as keyof typeof colors] || colors.INFO;
}


import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Shield, Sparkles, Zap, FileSearch, BarChart3, Github } from 'lucide-react';
import Link from 'next/link';

export default function HomePage() {
  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-50 to-slate-100 dark:from-slate-950 dark:to-slate-900">
      {/* Header */}
      <header className="border-b bg-white/50 backdrop-blur-sm dark:bg-slate-950/50">
        <div className="container mx-auto px-4 py-4 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Shield className="h-8 w-8 text-blue-600" />
            <h1 className="text-2xl font-bold">API Security Guardian</h1>
          </div>
          <nav className="flex items-center gap-4">
            <Link href="/dashboard">
              <Button variant="ghost">Dashboard</Button>
            </Link>
            <Link href="https://github.com/vtb/api-security-guardian" target="_blank">
              <Button variant="outline" size="icon">
                <Github className="h-5 w-5" />
              </Button>
            </Link>
          </nav>
        </div>
      </header>

      {/* Hero */}
      <section className="container mx-auto px-4 py-20 text-center">
        <div className="max-w-4xl mx-auto space-y-6">
          <div className="inline-block rounded-full bg-blue-100 px-4 py-2 text-sm font-semibold text-blue-700 dark:bg-blue-900 dark:text-blue-300">
            VTB API Hackathon 2025
          </div>
          <h2 className="text-5xl font-bold tracking-tight">
            Автоматический анализ <br />
            <span className="text-blue-600">безопасности API</span>
          </h2>
          <p className="text-xl text-muted-foreground">
            Находите уязвимости из OWASP API Top 10 с помощью AI-powered анализа. 
            Защитите свои API до того, как это сделают злоумышленники.
          </p>
          <div className="flex gap-4 justify-center pt-4">
            <Link href="/dashboard/scans/new">
              <Button size="lg" className="gap-2">
                <Zap className="h-5 w-5" />
                Начать сканирование
              </Button>
            </Link>
            <Link href="/docs">
              <Button size="lg" variant="outline">
                Документация
              </Button>
            </Link>
          </div>
        </div>
      </section>

      {/* Features */}
      <section className="container mx-auto px-4 py-20">
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          <Card>
            <CardHeader>
              <div className="rounded-full bg-blue-100 w-12 h-12 flex items-center justify-center mb-2">
                <Sparkles className="h-6 w-6 text-blue-600" />
              </div>
              <CardTitle>AI-Powered анализ</CardTitle>
              <CardDescription>
                GPT-4 находит логические уязвимости, которые пропускают традиционные сканеры
              </CardDescription>
            </CardHeader>
          </Card>

          <Card>
            <CardHeader>
              <div className="rounded-full bg-orange-100 w-12 h-12 flex items-center justify-center mb-2">
                <Shield className="h-6 w-6 text-orange-600" />
              </div>
              <CardTitle>OWASP API Top 10</CardTitle>
              <CardDescription>
                Полное покрытие всех 10 категорий уязвимостей OWASP API Security 2023
              </CardDescription>
            </CardHeader>
          </Card>

          <Card>
            <CardHeader>
              <div className="rounded-full bg-green-100 w-12 h-12 flex items-center justify-center mb-2">
                <Zap className="h-6 w-6 text-green-600" />
              </div>
              <CardTitle>Быстрый анализ</CardTitle>
              <CardDescription>
                Сканирование 30 эндпоинтов менее чем за 5 минут с детальными рекомендациями
              </CardDescription>
            </CardHeader>
          </Card>

          <Card>
            <CardHeader>
              <div className="rounded-full bg-purple-100 w-12 h-12 flex items-center justify-center mb-2">
                <FileSearch className="h-6 w-6 text-purple-600" />
              </div>
              <CardTitle>Умный фаззинг</CardTitle>
              <CardDescription>
                AI генерирует нестандартные тест-кейсы для поиска скрытых уязвимостей
              </CardDescription>
            </CardHeader>
          </Card>

          <Card>
            <CardHeader>
              <div className="rounded-full bg-red-100 w-12 h-12 flex items-center justify-center mb-2">
                <BarChart3 className="h-6 w-6 text-red-600" />
              </div>
              <CardTitle>Детальные отчеты</CardTitle>
              <CardDescription>
                HTML, PDF, JSON и SARIF отчеты с примерами кода и PoC эксплоитами
              </CardDescription>
            </CardHeader>
          </Card>

          <Card>
            <CardHeader>
              <div className="rounded-full bg-cyan-100 w-12 h-12 flex items-center justify-center mb-2">
                <Github className="h-6 w-6 text-cyan-600" />
              </div>
              <CardTitle>CI/CD интеграция</CardTitle>
              <CardDescription>
                GitHub Actions, GitLab CI, Jenkins - готовые примеры интеграции
              </CardDescription>
            </CardHeader>
          </Card>
        </div>
      </section>

      {/* CTA */}
      <section className="container mx-auto px-4 py-20">
        <Card className="bg-gradient-to-r from-blue-600 to-purple-600 text-white border-0">
          <CardContent className="p-12 text-center space-y-6">
            <h3 className="text-3xl font-bold">Готовы защитить свои API?</h3>
            <p className="text-lg opacity-90 max-w-2xl mx-auto">
              Начните с бесплатного сканирования прямо сейчас. Без регистрации, без кредитной карты.
            </p>
            <Link href="/dashboard/scans/new">
              <Button size="lg" variant="secondary" className="gap-2">
                <Shield className="h-5 w-5" />
                Начать бесплатное сканирование
              </Button>
            </Link>
          </CardContent>
        </Card>
      </section>

      {/* Footer */}
      <footer className="border-t bg-white/50 backdrop-blur-sm dark:bg-slate-950/50">
        <div className="container mx-auto px-4 py-8">
          <div className="flex flex-col md:flex-row items-center justify-between gap-4">
            <div className="flex items-center gap-2">
              <Shield className="h-6 w-6 text-blue-600" />
              <span className="font-semibold">API Security Guardian</span>
            </div>
            <p className="text-sm text-muted-foreground">
              Made with ❤️ by Mojarung for VTB API Hackathon 2025
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
}


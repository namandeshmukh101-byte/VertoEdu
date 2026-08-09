import { useEffect, useState } from 'react'
import { useSearchParams, Navigate } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { Button } from '@/components/ui/button'
import { GraduationCap, Sparkles, ArrowRight, Loader2, AlertCircle } from 'lucide-react'

/**
 * LoginPage — Authentication page with Google OAuth login.
 * Displays VertoEdu branding, "Continue with Google" button, loading and error states.
 */
export default function LoginPage() {
  const { isAuthenticated, loading: authLoading, user, login } = useAuth()
  const [searchParams] = useSearchParams()
  const [loginLoading, setLoginLoading] = useState(false)

  const error = searchParams.get('error')

  // Redirect map based on role
  const dashboardPath = {
    ADMIN: '/admin',
    TEACHER: '/teacher',
    PARENT: '/parent',
  }

  // If already authenticated, redirect to appropriate dashboard
  if (!authLoading && isAuthenticated && user) {
    return <Navigate to={dashboardPath[user.role] || '/'} replace />
  }

  const handleLogin = () => {
    setLoginLoading(true)
    login()
  }

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-background px-4">
      <div className="flex flex-col items-center gap-6 max-w-lg text-center">
        {/* Logo icon */}
        <div className="relative">
          <div className="w-20 h-20 rounded-2xl bg-primary/10 flex items-center justify-center">
            <GraduationCap className="w-10 h-10 text-primary" />
          </div>
          <div className="absolute -top-1 -right-1 w-6 h-6 rounded-full bg-primary flex items-center justify-center">
            <Sparkles className="w-3 h-3 text-white" />
          </div>
        </div>

        {/* Project name */}
        <h1 className="text-4xl sm:text-5xl font-bold tracking-tight text-foreground">
          Verto<span className="text-primary">Edu</span>
        </h1>

        {/* Tagline */}
        <p className="text-lg text-muted-foreground">Where AI Meets Education</p>

        {/* Description */}
        <p className="text-sm text-muted-foreground max-w-md leading-relaxed">
          Sign in with your Google account to access the AI-powered School Operations
          Platform.
        </p>

        {/* Error message */}
        {error && (
          <div className="flex items-center gap-2 rounded-lg border border-destructive/30 bg-destructive/5 px-4 py-3 text-sm text-destructive">
            <AlertCircle className="h-4 w-4 shrink-0" />
            <span>{decodeURIComponent(error)}</span>
          </div>
        )}

        {/* Continue with Google button */}
        <Button
          size="lg"
          className="mt-2 gap-2 px-8 cursor-pointer"
          onClick={handleLogin}
          disabled={loginLoading || authLoading}
        >
          {loginLoading || authLoading ? (
            <Loader2 className="w-5 h-5 animate-spin" />
          ) : (
            <svg className="w-5 h-5" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path
                d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92a5.06 5.06 0 0 1-2.2 3.32v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.1z"
                fill="#4285F4"
              />
              <path
                d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                fill="#34A853"
              />
              <path
                d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
                fill="#FBBC05"
              />
              <path
                d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                fill="#EA4335"
              />
            </svg>
          )}
          {loginLoading ? 'Redirecting...' : 'Continue with Google'}
          {!loginLoading && !authLoading && <ArrowRight className="w-4 h-4" />}
        </Button>

        {/* Footer note */}
        <p className="text-xs text-muted-foreground mt-8">
          Hackathon 2026 • Team VertoEdu
        </p>
      </div>
    </div>
  )
}

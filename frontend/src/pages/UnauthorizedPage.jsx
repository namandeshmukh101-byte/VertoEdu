import { useNavigate } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { Button } from '@/components/ui/button'
import { ShieldX, ArrowLeft, LogOut } from 'lucide-react'

/**
 * UnauthorizedPage — Shown when a user tries to access a route they don't have permission for.
 * Provides options to go back to their own dashboard or logout.
 */
export default function UnauthorizedPage() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const dashboardPath = {
    ADMIN: '/admin',
    TEACHER: '/teacher',
    PARENT: '/parent',
  }

  const handleGoBack = () => {
    if (user) {
      navigate(dashboardPath[user.role] || '/')
    } else {
      navigate('/')
    }
  }

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-background px-4">
      <div className="flex flex-col items-center gap-6 max-w-md text-center">
        {/* Icon */}
        <div className="w-20 h-20 rounded-2xl bg-destructive/10 flex items-center justify-center">
          <ShieldX className="w-10 h-10 text-destructive" />
        </div>

        {/* Title */}
        <h1 className="text-3xl font-bold tracking-tight text-foreground">
          Access Denied
        </h1>

        {/* Message */}
        <p className="text-muted-foreground leading-relaxed">
          You don&apos;t have permission to access this page.
          {user && (
            <span>
              {' '}Your current role is{' '}
              <span className="font-semibold text-foreground">{user.role}</span>.
            </span>
          )}
        </p>

        {/* Actions */}
        <div className="flex gap-3 mt-2">
          <Button variant="outline" onClick={handleGoBack} className="gap-2">
            <ArrowLeft className="w-4 h-4" />
            Go to Dashboard
          </Button>
          <Button variant="ghost" onClick={logout} className="gap-2 text-muted-foreground">
            <LogOut className="w-4 h-4" />
            Logout
          </Button>
        </div>
      </div>
    </div>
  )
}

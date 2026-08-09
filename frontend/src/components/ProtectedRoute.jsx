import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { Loader2 } from 'lucide-react'

/**
 * ProtectedRoute — Route guard component that checks authentication and role.
 * Redirects to /login if not authenticated, or /unauthorized if wrong role.
 *
 * @param {string} role - Required role to access this route (e.g. "ADMIN")
 * @param {React.ReactNode} children - Child components to render if authorized
 */
export default function ProtectedRoute({ role, children }) {
  const { user, loading, isAuthenticated } = useAuth()
  const location = useLocation()

  // Show loading spinner while checking session
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="flex flex-col items-center gap-3">
          <Loader2 className="w-8 h-8 text-primary animate-spin" />
          <p className="text-sm text-muted-foreground">Verifying access...</p>
        </div>
      </div>
    )
  }

  // Not authenticated — redirect to login
  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />
  }

  // Authenticated but wrong role — redirect to unauthorized
  if (role && user?.role !== role) {
    return <Navigate to="/unauthorized" replace />
  }

  return children
}

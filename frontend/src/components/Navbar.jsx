import { useAuth } from '@/contexts/AuthContext'
import { GraduationCap, Sparkles, LogOut, User } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Link } from 'react-router-dom'

/**
 * Navbar — Top navigation bar shown on all authenticated pages.
 * Displays VertoEdu branding, user info with role badge, and logout button.
 */
export default function Navbar() {
  const { user, logout } = useAuth()

  if (!user) return null

  const roleBadgeColors = {
    ADMIN: 'bg-red-100 text-red-700 border-red-200',
    TEACHER: 'bg-blue-100 text-blue-700 border-blue-200',
    PARENT: 'bg-green-100 text-green-700 border-green-200',
  }

  const dashboardPath = {
    ADMIN: '/admin',
    TEACHER: '/teacher',
    PARENT: '/parent',
  }

  return (
    <nav className="sticky top-0 z-50 w-full border-b border-border bg-card/95 backdrop-blur supports-[backdrop-filter]:bg-card/60">
      <div className="mx-auto flex h-14 max-w-7xl items-center justify-between px-4 sm:px-6 lg:px-8">
        {/* Logo and brand */}
        <Link
          to={dashboardPath[user.role] || '/'}
          className="flex items-center gap-2 transition-opacity hover:opacity-80"
        >
          <div className="relative">
            <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary/10">
              <GraduationCap className="h-4 w-4 text-primary" />
            </div>
            <div className="absolute -right-0.5 -top-0.5 flex h-3 w-3 items-center justify-center rounded-full bg-primary">
              <Sparkles className="h-1.5 w-1.5 text-white" />
            </div>
          </div>
          <span className="text-lg font-semibold tracking-tight text-foreground">
            Verto<span className="text-primary">Edu</span>
          </span>
        </Link>

        {/* User info and actions */}
        <div className="flex items-center gap-3">
          {/* Role badge */}
          <span
            className={`hidden rounded-full border px-2.5 py-0.5 text-xs font-medium sm:inline-block ${
              roleBadgeColors[user.role] || 'bg-gray-100 text-gray-700 border-gray-200'
            }`}
          >
            {user.role}
          </span>

          {/* User avatar and name */}
          <div className="flex items-center gap-2">
            {user.profileImage ? (
              <img
                src={user.profileImage}
                alt={user.fullName}
                className="h-7 w-7 rounded-full object-cover ring-2 ring-border"
                referrerPolicy="no-referrer"
              />
            ) : (
              <div className="flex h-7 w-7 items-center justify-center rounded-full bg-primary/10">
                <User className="h-3.5 w-3.5 text-primary" />
              </div>
            )}
            <span className="hidden text-sm font-medium text-foreground md:inline-block">
              {user.fullName}
            </span>
          </div>

          {/* Logout button */}
          <Button
            variant="ghost"
            size="sm"
            onClick={logout}
            className="gap-1.5 text-muted-foreground hover:text-foreground"
          >
            <LogOut className="h-3.5 w-3.5" />
            <span className="hidden sm:inline">Logout</span>
          </Button>
        </div>
      </div>
    </nav>
  )
}

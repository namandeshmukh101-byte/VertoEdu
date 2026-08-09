import { useAuth } from '@/contexts/AuthContext'
import Navbar from '@/components/Navbar'
import { GraduationCap, Sparkles, Construction } from 'lucide-react'

/**
 * DashboardPlaceholder — Temporary placeholder page for role-based dashboards.
 * Will be replaced with real dashboards in future prompts.
 *
 * @param {string} role - The role this dashboard is for (ADMIN, TEACHER, PARENT)
 */
export default function DashboardPlaceholder({ role }) {
  const { user } = useAuth()

  const roleInfo = {
    ADMIN: {
      title: 'Admin Dashboard',
      description: 'School administration and management will be available here.',
      color: 'text-red-600',
      bg: 'bg-red-50',
    },
    TEACHER: {
      title: 'Teacher Dashboard',
      description: 'Class management, attendance, and grades will be available here.',
      color: 'text-blue-600',
      bg: 'bg-blue-50',
    },
    PARENT: {
      title: 'Parent Dashboard',
      description: 'Student progress, attendance, and notices will be available here.',
      color: 'text-green-600',
      bg: 'bg-green-50',
    },
  }

  const info = roleInfo[role] || roleInfo.PARENT

  return (
    <div className="min-h-screen bg-background">
      <Navbar />

      <main className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
        <div className="flex flex-col items-center justify-center gap-6 text-center py-20">
          {/* Icon */}
          <div className="relative">
            <div className={`w-16 h-16 rounded-2xl ${info.bg} flex items-center justify-center`}>
              <Construction className={`w-8 h-8 ${info.color}`} />
            </div>
          </div>

          {/* Title */}
          <h1 className="text-3xl font-bold tracking-tight text-foreground">
            {info.title}
          </h1>

          {/* Welcome */}
          {user && (
            <p className="text-lg text-muted-foreground">
              Welcome, <span className="font-medium text-foreground">{user.fullName}</span>
            </p>
          )}

          {/* Description */}
          <p className="text-sm text-muted-foreground max-w-md leading-relaxed">
            {info.description}
            <br />
            <span className="text-xs mt-2 inline-block">
              This dashboard will be built in upcoming prompts.
            </span>
          </p>
        </div>
      </main>
    </div>
  )
}

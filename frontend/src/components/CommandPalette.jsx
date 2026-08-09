import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { motion, AnimatePresence } from 'framer-motion'
import { Search, LogOut, Settings, LayoutDashboard, X, Users, BookOpen, Clock, ScanText } from 'lucide-react'
import { useAuth } from '@/contexts/AuthContext'

export default function CommandPalette() {
  const [isOpen, setIsOpen] = useState(false)
  const [query, setQuery] = useState('')
  const { user, logout, isAuthenticated } = useAuth()
  const navigate = useNavigate()

  useEffect(() => {
    const handleKeyDown = (e) => {
      if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
        e.preventDefault()
        setIsOpen((prev) => !prev)
      }
      if (e.key === 'Escape') {
        setIsOpen(false)
      }
    }
    window.addEventListener('keydown', handleKeyDown)
    return () => window.removeEventListener('keydown', handleKeyDown)
  }, [])

  if (!isAuthenticated) return null

  // Pre-defined static actions based on role
  const actions = [
    { id: 'dashboard', name: 'Dashboard', icon: LayoutDashboard, path: `/${user?.role?.toLowerCase()}` },
    { id: 'profile', name: 'My Profile', icon: Settings, path: `/${user?.role?.toLowerCase()}/profile` },
  ]

  if (user?.role === 'ADMIN') {
    actions.push({ id: 'students', name: 'Manage Students', icon: Users, path: '/admin/students' })
    actions.push({ id: 'ocr', name: 'Document OCR', icon: ScanText, path: '/admin/ocr' })
  } else if (user?.role === 'TEACHER') {
    actions.push({ id: 'attendance', name: 'Mark Attendance', icon: Clock, path: '/teacher/attendance' })
    actions.push({ id: 'results', name: 'Enter Results', icon: BookOpen, path: '/teacher/marks' })
  }

  const filteredActions = actions.filter(action => action.name.toLowerCase().includes(query.toLowerCase()))

  const handleAction = (path) => {
    setIsOpen(false)
    navigate(path)
    setQuery('')
  }

  const handleLogout = () => {
    setIsOpen(false)
    logout()
  }

  return (
    <AnimatePresence>
      {isOpen && (
        <>
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            onClick={() => setIsOpen(false)}
            className="fixed inset-0 z-[100] bg-background/80 backdrop-blur-sm"
          />
          <motion.div
            initial={{ opacity: 0, scale: 0.95, y: -20 }}
            animate={{ opacity: 1, scale: 1, y: 0 }}
            exit={{ opacity: 0, scale: 0.95, y: -20 }}
            className="fixed left-[50%] top-[20%] z-[101] w-full max-w-xl -translate-x-1/2 rounded-2xl border border-border bg-card shadow-2xl overflow-hidden flex flex-col"
          >
            <div className="flex items-center border-b border-border px-4 py-3">
              <Search className="w-5 h-5 text-muted-foreground mr-3" />
              <input
                autoFocus
                className="flex-1 bg-transparent text-lg outline-none placeholder:text-muted-foreground text-foreground"
                placeholder="Type a command or search..."
                value={query}
                onChange={(e) => setQuery(e.target.value)}
              />
              <div className="flex items-center gap-2">
                <span className="text-xs bg-muted text-muted-foreground px-2 py-1 rounded-md border border-border">ESC</span>
                <button onClick={() => setIsOpen(false)} className="text-muted-foreground hover:text-foreground">
                  <X className="w-5 h-5" />
                </button>
              </div>
            </div>

            <div className="max-h-[60vh] overflow-y-auto p-2 space-y-1">
              <div className="px-3 py-2 text-xs font-semibold text-muted-foreground uppercase tracking-wider">
                Quick Actions
              </div>
              
              {filteredActions.length === 0 ? (
                <div className="px-4 py-8 text-center text-muted-foreground text-sm">
                  No results found for "{query}".
                </div>
              ) : (
                filteredActions.map((action) => (
                  <button
                    key={action.id}
                    onClick={() => handleAction(action.path)}
                    className="w-full flex items-center gap-3 px-3 py-3 rounded-xl hover:bg-muted text-left transition-colors text-foreground"
                  >
                    <action.icon className="w-5 h-5 text-muted-foreground" />
                    <span className="font-medium">{action.name}</span>
                  </button>
                ))
              )}

              <div className="px-3 py-2 mt-4 text-xs font-semibold text-muted-foreground uppercase tracking-wider">
                Account
              </div>
              <button
                onClick={handleLogout}
                className="w-full flex items-center gap-3 px-3 py-3 rounded-xl hover:bg-destructive/10 text-destructive text-left transition-colors"
              >
                <LogOut className="w-5 h-5" />
                <span className="font-medium">Sign Out</span>
              </button>
            </div>
          </motion.div>
        </>
      )}
    </AnimatePresence>
  )
}

import { createContext, useContext, useState, useEffect, useCallback } from 'react'
import authService from '@/services/authService'

const AuthContext = createContext(null)

/**
 * AuthProvider — Manages authentication state across the application.
 * On mount, checks for existing session via /auth/me.
 * Provides user, loading, login, and logout to all children.
 */
export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  /**
   * Check if the user has an active session (JWT cookie).
   */
  const checkAuth = useCallback(async () => {
    try {
      const response = await authService.getCurrentUser()
      if (response.success && response.data) {
        setUser(response.data)
      } else {
        setUser(null)
      }
    } catch {
      setUser(null)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    checkAuth()
  }, [checkAuth])

  /**
   * Redirect to Google OAuth login via the backend.
   */
  const login = useCallback(() => {
    window.location.href = authService.getGoogleLoginUrl()
  }, [])

  /**
   * Logout — clear session on backend, reset state, redirect to landing.
   */
  const logout = useCallback(async () => {
    try {
      await authService.logout()
    } catch {
      // Even if the API call fails, clear local state
    }
    setUser(null)
    window.location.href = '/'
  }, [])

  /**
   * Check if the current user has a specific role.
   */
  const hasRole = useCallback(
    (role) => {
      return user?.role === role
    },
    [user]
  )

  const value = {
    user,
    loading,
    isAuthenticated: !!user,
    login,
    logout,
    hasRole,
    checkAuth,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

/**
 * useAuth — Hook to access authentication context.
 */
export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

export default AuthContext

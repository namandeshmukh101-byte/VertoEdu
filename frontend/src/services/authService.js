import api from './api'

/**
 * authService — API calls for authentication operations.
 */
const authService = {
  /**
   * Get the currently authenticated user's information.
   * Returns user data if authenticated, throws if not.
   */
  getCurrentUser: async () => {
    const response = await api.get('/auth/me')
    return response.data
  },

  /**
   * Logout the current user (clears JWT cookie on backend).
   */
  logout: async () => {
    const response = await api.post('/auth/logout')
    return response.data
  },

  /**
   * Get the backend OAuth2 login URL for Google.
   * The backend handles the full OAuth2 dance server-side.
   */
  getGoogleLoginUrl: () => {
    const baseUrl = import.meta.env.VITE_API_BASE_URL || '/api'
    return `${baseUrl}/oauth2/authorization/google`
  },
}

export default authService

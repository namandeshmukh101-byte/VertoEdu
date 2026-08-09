import axios from 'axios'

/**
 * Axios instance configured for VertoEdu API.
 * Base URL is loaded from environment variables.
 */
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})

// Request interceptor
api.interceptors.request.use(
  (config) => {
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor
api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response) {
      const { status } = error.response

      if (status === 401) {
        // Redirect to login on unauthorized if not already there
        if (window.location.pathname !== '/login') {
          window.location.href = '/login'
        }
      }

      if (status === 403) {
        // Redirect to unauthorized page
        window.location.href = '/unauthorized'
      }
    }

    return Promise.reject(error)
  }
)

export default api

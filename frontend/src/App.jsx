import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { HelmetProvider } from 'react-helmet-async'
import { AuthProvider, useAuth } from '@/contexts/AuthContext'
import PublicLayout from '@/components/PublicLayout'
import ProtectedRoute from '@/components/ProtectedRoute'
import CommandPalette from '@/components/CommandPalette'
import LandingPage from '@/pages/LandingPage'
import LoginPage from '@/pages/LoginPage'
import UnauthorizedPage from '@/pages/UnauthorizedPage'

import PrivacyPolicy from '@/public-pages/PrivacyPolicy'
import Terms from '@/public-pages/Terms'
import Cookies from '@/public-pages/Cookies'
import About from '@/public-pages/About'
import Contact from '@/public-pages/Contact'
import FAQ from '@/public-pages/FAQ'
import Accessibility from '@/public-pages/Accessibility'
import ResponsibleAI from '@/public-pages/ResponsibleAI'
import Security from '@/public-pages/Security'
import AboutDeveloper from '@/public-pages/AboutDeveloper'

import DashboardPlaceholder from '@/pages/DashboardPlaceholder'
import TeacherDashboard from '@/pages/teacher/TeacherDashboard'
import AttendancePage from '@/pages/teacher/AttendancePage'
import MarksEntryPage from '@/pages/teacher/MarksEntryPage'
import ProfilePage from '@/pages/teacher/ProfilePage'
import ParentDashboard from '@/pages/parent/ParentDashboard'
import ParentAttendancePage from '@/pages/parent/AttendancePage'
import ParentResultsPage from '@/pages/parent/ResultsPage'
import ParentProfilePage from '@/pages/parent/ProfilePage'
import AdminDashboard from '@/pages/admin/AdminDashboard'
import AdminStudents from '@/pages/admin/AdminStudents'
import AdminTeachers from '@/pages/admin/AdminTeachers'
import AdminParents from '@/pages/admin/AdminParents'
import OcrDashboard from '@/pages/admin/ocr/OcrDashboard'
import UploadDocument from '@/pages/admin/ocr/UploadDocument'
import ProcessingPipeline from '@/pages/admin/ocr/ProcessingPipeline'

/**
 * SmartLanding — Redirects authenticated users to their dashboard,
 * shows landing page to unauthenticated users.
 */
function SmartLanding() {
  const { user, loading, isAuthenticated } = useAuth()

  if (loading) return null

  if (isAuthenticated && user) {
    const dashboardPath = {
      ADMIN: '/admin',
      TEACHER: '/teacher',
      PARENT: '/parent',
    }
    return <Navigate to={dashboardPath[user.role] || '/'} replace />
  }

  return <LandingPage />
}

/**
 * App — Root application component with React Router and AuthProvider.
 * Routes are protected by role-based access control.
 */
function App() {
  return (
    <HelmetProvider>
      <BrowserRouter>
        <AuthProvider>
          <CommandPalette />
          <Routes>
            {/* Public routes wrapped in PublicLayout */}
            <Route path="/" element={<PublicLayout><SmartLanding /></PublicLayout>} />
            <Route path="/privacy-policy" element={<PublicLayout><PrivacyPolicy /></PublicLayout>} />
            <Route path="/terms" element={<PublicLayout><Terms /></PublicLayout>} />
            <Route path="/cookies" element={<PublicLayout><Cookies /></PublicLayout>} />
            <Route path="/about" element={<PublicLayout><About /></PublicLayout>} />
            <Route path="/contact" element={<PublicLayout><Contact /></PublicLayout>} />
            <Route path="/faq" element={<PublicLayout><FAQ /></PublicLayout>} />
            <Route path="/accessibility" element={<PublicLayout><Accessibility /></PublicLayout>} />
            <Route path="/responsible-ai" element={<PublicLayout><ResponsibleAI /></PublicLayout>} />
            <Route path="/security" element={<PublicLayout><Security /></PublicLayout>} />
            <Route path="/about-developer" element={<PublicLayout><AboutDeveloper /></PublicLayout>} />
            
            <Route path="/login" element={<LoginPage />} />
            <Route path="/unauthorized" element={<UnauthorizedPage />} />

          {/* Admin routes (ADMIN only) */}
          <Route
            path="/admin"
            element={
              <ProtectedRoute role="ADMIN">
                <AdminDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/students"
            element={
              <ProtectedRoute role="ADMIN">
                <AdminStudents />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/teachers"
            element={
              <ProtectedRoute role="ADMIN">
                <AdminTeachers />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/parents"
            element={
              <ProtectedRoute role="ADMIN">
                <AdminParents />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/ocr"
            element={
              <ProtectedRoute role="ADMIN">
                <OcrDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/ocr/upload"
            element={
              <ProtectedRoute role="ADMIN">
                <UploadDocument />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/ocr/process/:id"
            element={
              <ProtectedRoute role="ADMIN">
                <ProcessingPipeline />
              </ProtectedRoute>
            }
          />

          {/* Teacher routes (TEACHER only) */}
          <Route
            path="/teacher"
            element={<ProtectedRoute role="TEACHER"><TeacherDashboard /></ProtectedRoute>}
          />
          <Route
            path="/teacher/attendance"
            element={<ProtectedRoute role="TEACHER"><AttendancePage /></ProtectedRoute>}
          />
          <Route
            path="/teacher/marks"
            element={<ProtectedRoute role="TEACHER"><MarksEntryPage /></ProtectedRoute>}
          />
          <Route
            path="/teacher/profile"
            element={<ProtectedRoute role="TEACHER"><ProfilePage /></ProtectedRoute>}
          />

          {/* Parent routes (PARENT only) */}
          <Route
            path="/parent"
            element={
              <ProtectedRoute role="PARENT">
                <ParentDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/parent/attendance"
            element={
              <ProtectedRoute role="PARENT">
                <ParentAttendancePage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/parent/results"
            element={
              <ProtectedRoute role="PARENT">
                <ParentResultsPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/parent/profile"
            element={
              <ProtectedRoute role="PARENT">
                <ParentProfilePage />
              </ProtectedRoute>
            }
          />

          {/* Catch-all redirect */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
    </HelmetProvider>
  )
}

export default App

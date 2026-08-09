import React, { useState, useEffect } from 'react'
import { FileText, Users, Settings, BookOpen, GraduationCap, LayoutDashboard, BrainCircuit } from 'lucide-react'
import { Link } from 'react-router-dom'
import axios from 'axios'
import DashboardLayout from '@/layouts/DashboardLayout'
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import { Skeleton } from '@/components/ui/skeleton'
import api from '@/services/api'

export default function AdminDashboard() {
  const [summary, setSummary] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchSummary = async () => {
      try {
        const response = await api.get('/admin/dashboard/summary')
        setSummary(response.data.data)
      } catch (err) {
        console.error('Failed to fetch dashboard summary', err)
      } finally {
        setLoading(false)
      }
    }
    fetchSummary()
  }, [])

  const sidebarLinks = [
    { title: 'Dashboard', href: '/admin', icon: LayoutDashboard },
    { title: 'Students', href: '/admin/students', icon: GraduationCap },
    { title: 'Teachers', href: '/admin/teachers', icon: Users },
    { title: 'Parents', href: '/admin/parents', icon: Users },
    { title: 'OCR Processing', href: '/admin/ocr', icon: FileText },
    { title: 'AI Review', href: '/admin/ocr/process', icon: BrainCircuit },
  ]

  const stats = [
    { title: 'Total Students', value: summary?.totalStudents || 0, icon: GraduationCap, color: 'text-emerald-600', bg: 'bg-emerald-50' },
    { title: 'Total Teachers', value: summary?.totalTeachers || 0, icon: Users, color: 'text-indigo-600', bg: 'bg-indigo-50' },
    { title: 'Total Parents', value: summary?.totalParents || 0, icon: Users, color: 'text-blue-600', bg: 'bg-blue-50' },
    { title: 'Pending OCRs', value: summary?.pendingOcr || 0, icon: FileText, color: 'text-amber-600', bg: 'bg-amber-50' },
    { title: 'Pending AI Reviews', value: summary?.pendingAiReviews || 0, icon: BrainCircuit, color: 'text-rose-600', bg: 'bg-rose-50' },
  ]

  const [systemStatus, setSystemStatus] = useState(null)
  useEffect(() => {
    const fetchStatus = async () => {
      try {
        const res = await api.get('/actuator/health')
        setSystemStatus(res.data.status)
      } catch (err) {
        setSystemStatus('DOWN')
      }
    }
    fetchStatus()
  }, [])

  return (
    <DashboardLayout sidebarLinks={sidebarLinks}>
      <div className="mb-8 flex justify-between items-end">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Admin Dashboard</h1>
          <p className="text-muted-foreground mt-2">Overview of the entire school system.</p>
        </div>
        <div className="flex items-center space-x-2">
           <span className="text-sm font-medium text-muted-foreground">System Status:</span>
           {systemStatus === 'UP' ? (
             <span className="px-2 py-1 bg-emerald-100 text-emerald-700 rounded-md text-xs font-bold">ONLINE</span>
           ) : (
             <span className="px-2 py-1 bg-red-100 text-red-700 rounded-md text-xs font-bold">{systemStatus || 'CHECKING'}</span>
           )}
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
        {loading ? (
          Array.from({ length: 5 }).map((_, i) => (
            <Card key={i}>
              <CardContent className="p-6">
                <Skeleton className="h-12 w-12 rounded-lg mb-4" />
                <Skeleton className="h-4 w-1/2 mb-2" />
                <Skeleton className="h-6 w-1/4" />
              </CardContent>
            </Card>
          ))
        ) : (
          stats.map((stat) => (
            <Card key={stat.title} className="hover:shadow-md transition-shadow">
              <CardContent className="p-6 flex flex-col justify-between">
                <div className="flex justify-between items-start mb-4">
                  <div className={`p-3 rounded-lg ${stat.bg}`}>
                    <stat.icon className={`w-6 h-6 ${stat.color}`} />
                  </div>
                </div>
                <div>
                  <p className="text-sm font-medium text-muted-foreground">{stat.title}</p>
                  <h3 className="text-3xl font-bold tracking-tight mt-1">{stat.value}</h3>
                </div>
              </CardContent>
            </Card>
          ))
        )}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>Quick Actions</CardTitle>
          </CardHeader>
          <CardContent className="grid grid-cols-2 gap-4">
            <Link to="/admin/ocr/upload" className="flex items-center justify-center p-4 border rounded-xl hover:bg-accent hover:text-accent-foreground transition-colors">
              <FileText className="w-5 h-5 mr-2" />
              Upload OCR
            </Link>
            <Link to="/admin/ocr" className="flex items-center justify-center p-4 border rounded-xl hover:bg-accent hover:text-accent-foreground transition-colors">
              <BrainCircuit className="w-5 h-5 mr-2" />
              Review AI
            </Link>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Recent Admissions</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <Skeleton className="h-32 w-full" />
            ) : summary?.recentAdmissions?.length > 0 ? (
              <ul className="space-y-4">
                {summary.recentAdmissions.map((admission, i) => (
                  <li key={i} className="flex justify-between items-center text-sm p-2 bg-muted/50 rounded-lg">
                    <span className="font-medium">{admission.name}</span>
                    <span className="text-muted-foreground text-xs">{admission.date}</span>
                  </li>
                ))}
              </ul>
            ) : (
              <div className="py-8 text-center text-muted-foreground border-2 border-dashed rounded-xl">
                No recent admissions
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </DashboardLayout>
  )
}

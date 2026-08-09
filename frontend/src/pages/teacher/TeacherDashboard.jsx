import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { CalendarCheck, BookOpen, UserCircle, LayoutDashboard, FileSpreadsheet } from 'lucide-react'
import api from '@/services/api'
import DashboardLayout from '@/layouts/DashboardLayout'
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Skeleton } from '@/components/ui/skeleton'

export default function TeacherDashboard() {
  const [classes, setClasses] = useState([])
  const [subjects, setSubjects] = useState([])
  const [summary, setSummary] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [classesRes, subjectsRes, summaryRes] = await Promise.all([
          api.get('/teacher/me/classes'),
          api.get('/teacher/me/subjects'),
          api.get('/teacher/summary')
        ])
        setClasses(classesRes.data.data || [])
        setSubjects(subjectsRes.data.data || [])
        setSummary(summaryRes.data.data || null)
      } catch (error) {
        console.error('Failed to fetch dashboard data', error)
      } finally {
        setLoading(false)
      }
    }
    fetchDashboardData()
  }, [])

  const sidebarLinks = [
    { title: 'Dashboard', href: '/teacher', icon: LayoutDashboard },
    { title: 'Attendance', href: '/teacher/attendance', icon: CalendarCheck },
    { title: 'Results', href: '/teacher/marks', icon: FileSpreadsheet },
    { title: 'Profile', href: '/teacher/profile', icon: UserCircle },
  ]

  return (
    <DashboardLayout sidebarLinks={sidebarLinks}>
      <div className="mb-8">
        <h1 className="text-3xl font-bold tracking-tight">Teacher Dashboard</h1>
        <p className="text-muted-foreground mt-2">Manage your classes, record attendance, and update marks.</p>
      </div>

      <div className="grid md:grid-cols-2 gap-6 mb-8">
        <Card className="hover:shadow-md transition-shadow border-primary/20 bg-primary/5">
          <CardHeader>
            <CardTitle className="text-primary flex items-center">
              <CalendarCheck className="mr-2 w-5 h-5" /> Today's Attendance
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex justify-between items-center text-sm p-3 bg-white rounded-md border">
              <span>Status:</span>
              <span className="font-bold">
                {summary?.attendanceDoneToday ? 'Recorded' : 'Pending Entry'}
              </span>
            </div>
            <Button className="w-full justify-start" asChild>
              <Link to="/teacher/attendance">Go to Attendance</Link>
            </Button>
          </CardContent>
        </Card>

        <Card className="hover:shadow-md transition-shadow border-primary/20 bg-primary/5">
          <CardHeader>
            <CardTitle className="text-primary flex items-center">
              <FileSpreadsheet className="mr-2 w-5 h-5" /> Result Entry
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex justify-between items-center text-sm p-3 bg-white rounded-md border">
              <span>Upcoming/Pending Exams:</span>
              <span className="font-bold">{summary?.pendingExamsCount || 0}</span>
            </div>
            <Button variant="secondary" className="w-full justify-start" asChild>
              <Link to="/teacher/marks">Exam Result Entry</Link>
            </Button>
          </CardContent>
        </Card>
      </div>

      <div className="grid md:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center">
              <BookOpen className="mr-2 w-5 h-5 text-muted-foreground" />
              Assigned Classes
            </CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <Skeleton className="h-24 w-full" />
            ) : classes.length === 0 ? (
              <div className="p-6 text-center text-muted-foreground border-2 border-dashed rounded-xl">
                No classes assigned yet.
              </div>
            ) : (
              <ul className="space-y-2">
                {classes.map(c => (
                  <li key={c.id} className="p-3 bg-muted/50 rounded-lg border flex justify-between items-center text-sm">
                    <span className="font-medium">Section {c.name}</span>
                  </li>
                ))}
              </ul>
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center">
              <FileSpreadsheet className="mr-2 w-5 h-5 text-muted-foreground" />
              Assigned Subjects
            </CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <Skeleton className="h-24 w-full" />
            ) : subjects.length === 0 ? (
              <div className="p-6 text-center text-muted-foreground border-2 border-dashed rounded-xl">
                No subjects assigned yet.
              </div>
            ) : (
              <ul className="space-y-2">
                {subjects.map(s => (
                  <li key={s.id} className="p-3 bg-muted/50 rounded-lg border flex justify-between items-center text-sm">
                    <span className="font-medium">{s.name} ({s.code})</span>
                  </li>
                ))}
              </ul>
            )}
          </CardContent>
        </Card>
      </div>
    </DashboardLayout>
  )
}

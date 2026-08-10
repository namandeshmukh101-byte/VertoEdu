import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { LayoutDashboard, CalendarCheck, FileSpreadsheet, UserCircle, BellRing, BookOpen } from 'lucide-react'
import DashboardLayout from '@/layouts/DashboardLayout'
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import { Avatar, AvatarFallback } from '@/components/ui/avatar'
import { Skeleton } from '@/components/ui/skeleton'
import { Button } from '@/components/ui/button'
import api from '@/services/api'

export default function ParentDashboard() {
  const [profile, setProfile] = useState(null)
  const [students, setStudents] = useState([])
  const [notices, setNotices] = useState([])
  const [exams, setExams] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [profileRes, studentsRes, noticesRes, examsRes] = await Promise.all([
          api.get('/parent/me'),
          api.get('/parent/me/students'),
          api.get('/notices'),
          api.get('/parent/exams')
        ])
        setProfile(profileRes.data?.data || null)
        setStudents(studentsRes.data?.data || [])
        setNotices(noticesRes.data?.data || [])
        setExams(examsRes.data?.data || [])
      } catch (err) {
        console.error('Error fetching dashboard data:', err)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  const sidebarLinks = [
    { title: 'Dashboard', href: '/parent', icon: LayoutDashboard },
    { title: 'Attendance', href: '/parent/attendance', icon: CalendarCheck },
    { title: 'Results', href: '/parent/results', icon: FileSpreadsheet },
    { title: 'Profile', href: '/parent/profile', icon: UserCircle },
  ]

  const getInitials = (first, last) => {
    return `${(first || '')[0] || ''}${(last || '')[0] || ''}`.toUpperCase()
  }

  return (
    <DashboardLayout sidebarLinks={sidebarLinks}>
      <div className="mb-8 flex justify-between items-end">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Parent Dashboard</h1>
          <p className="text-muted-foreground mt-2">Welcome back, {profile?.firstName || 'Parent'}. View your children's progress.</p>
        </div>
      </div>

      <div className="mb-8">
        <h2 className="text-xl font-semibold mb-4 text-foreground">Linked Students</h2>
        {loading ? (
          <div className="grid md:grid-cols-2 gap-6">
            <Skeleton className="h-40 w-full rounded-xl" />
            <Skeleton className="h-40 w-full rounded-xl" />
          </div>
        ) : students.length > 0 ? (
          <div className="grid md:grid-cols-2 gap-6">
            {students.map(student => (
              <Card key={student.id} className="hover:shadow-md transition-shadow">
                <CardHeader className="bg-muted/30 border-b border-border/50 pb-4">
                  <div className="flex items-center space-x-4">
                    <Avatar className="h-12 w-12 bg-primary/10 text-primary border-primary/20">
                      <AvatarFallback className="font-bold text-lg bg-transparent">
                        {getInitials(student.firstName, student.lastName)}
                      </AvatarFallback>
                    </Avatar>
                    <div>
                      <CardTitle className="text-lg">{student.firstName} {student.lastName}</CardTitle>
                      <p className="text-sm text-muted-foreground">Roll No: {student.scholarNumber}</p>
                    </div>
                  </div>
                </CardHeader>
                <CardContent className="pt-4">
                  <div className="grid grid-cols-2 gap-4 text-sm mb-6">
                    <div>
                      <span className="block text-muted-foreground text-xs uppercase tracking-wider mb-1">Class</span>
                      <span className="font-medium text-foreground">{student.className} - {student.sectionName}</span>
                    </div>
                    <div>
                      <span className="block text-muted-foreground text-xs uppercase tracking-wider mb-1">Academic Year</span>
                      <span className="font-medium text-foreground">2026-2027</span>
                    </div>
                  </div>
                  <div className="flex space-x-3">
                    <Button variant="secondary" className="flex-1" asChild>
                      <Link to={`/parent/attendance?studentId=${student.id}`}>Attendance</Link>
                    </Button>
                    <Button variant="outline" className="flex-1" asChild>
                      <Link to={`/parent/results?studentId=${student.id}`}>Results</Link>
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        ) : (
          <div className="p-12 text-center text-muted-foreground border-2 border-dashed rounded-xl">
            No students linked to your account yet.
          </div>
        )}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <CardHeader>
            <CardTitle className="text-lg flex items-center">
              <BellRing className="w-5 h-5 mr-2 text-amber-500" /> Recent Notices
            </CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
               <Skeleton className="h-16 w-full" />
            ) : notices.length > 0 ? (
               <ul className="space-y-3">
                 {notices.map(notice => (
                   <li key={notice.id} className="border-b pb-2 last:border-0 last:pb-0">
                     <p className="font-semibold text-sm">{notice.title}</p>
                     <p className="text-xs text-muted-foreground">{new Date(notice.createdAt).toLocaleDateString()}</p>
                   </li>
                 ))}
               </ul>
            ) : (
              <p className="text-sm text-muted-foreground">No new notices from the school administration.</p>
            )}
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader>
            <CardTitle className="text-lg flex items-center">
              <BookOpen className="w-5 h-5 mr-2 text-indigo-500" /> Upcoming Exams
            </CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
               <Skeleton className="h-16 w-full" />
            ) : exams.length > 0 ? (
               <ul className="space-y-3">
                 {exams.map(exam => (
                   <li key={exam.id} className="border-b pb-2 last:border-0 last:pb-0 flex justify-between items-center">
                     <span className="font-semibold text-sm">{exam.name}</span>
                   </li>
                 ))}
               </ul>
            ) : (
              <p className="text-sm text-muted-foreground">No upcoming examinations scheduled.</p>
            )}
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader>
            <CardTitle className="text-lg flex items-center">
              <FileSpreadsheet className="w-5 h-5 mr-2 text-emerald-500" /> Fees Status
            </CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-sm text-muted-foreground">No pending fee dues for the current term.</p>
          </CardContent>
        </Card>
      </div>
    </DashboardLayout>
  )
}

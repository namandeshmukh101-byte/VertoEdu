import { useState, useEffect } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import api from '@/services/api'

export default function AttendancePage() {
  const [searchParams] = useSearchParams()
  const defaultStudentId = searchParams.get('studentId') || ''
  
  const [students, setStudents] = useState([])
  const [selectedStudent, setSelectedStudent] = useState(defaultStudentId)
  const [attendance, setAttendance] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchStudents = async () => {
      try {
        const res = await api.get('/parent/me/students')
        setStudents(res.data?.data || [])
        if (!selectedStudent && res.data.data.length > 0) {
          setSelectedStudent(res.data.data[0].id.toString())
        }
      } catch (err) {
        console.error('Error fetching students:', err)
      } finally {
        setLoading(false)
      }
    }
    fetchStudents()
  }, [])

  useEffect(() => {
    if (!selectedStudent) return
    const fetchAttendance = async () => {
      try {
        const res = await api.get(`/parent/students/${selectedStudent}/attendance`)
        setAttendance(res.data?.data || [])
      } catch (err) {
        console.error('Error fetching attendance:', err)
      }
    }
    fetchAttendance()
  }, [selectedStudent])

  // Calculate percentage
  const totalDays = attendance.length
  const presentDays = attendance.filter(a => a.status === 'PRESENT').length
  const percentage = totalDays > 0 ? Math.round((presentDays / totalDays) * 100) : 0

  if (loading) return <div className="p-8 text-center">Loading...</div>

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto space-y-6">
        
        <div className="flex justify-between items-center">
          <h1 className="text-3xl font-bold text-gray-900">Attendance History</h1>
          <Link to="/parent" className="text-indigo-600 hover:text-indigo-800 font-medium">
            &larr; Back to Dashboard
          </Link>
        </div>

        {/* Student Selector */}
        <Card className="shadow-sm">
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <label className="font-medium text-gray-700">Select Student:</label>
              <select 
                className="block w-64 rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                value={selectedStudent}
                onChange={(e) => setSelectedStudent(e.target.value)}
              >
                {students.map(s => (
                  <option key={s.id} value={s.id}>{s.firstName} {s.lastName} ({s.className})</option>
                ))}
              </select>
            </div>
          </CardContent>
        </Card>

        {/* Summary */}
        {selectedStudent && (
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <Card className="shadow-sm bg-indigo-50 border-indigo-100">
              <CardContent className="p-6 text-center">
                <p className="text-indigo-600 font-medium text-sm">Total Working Days</p>
                <p className="text-3xl font-bold text-indigo-900 mt-2">{totalDays}</p>
              </CardContent>
            </Card>
            <Card className="shadow-sm bg-green-50 border-green-100">
              <CardContent className="p-6 text-center">
                <p className="text-green-600 font-medium text-sm">Days Present</p>
                <p className="text-3xl font-bold text-green-900 mt-2">{presentDays}</p>
              </CardContent>
            </Card>
            <Card className="shadow-sm bg-blue-50 border-blue-100">
              <CardContent className="p-6 text-center">
                <p className="text-blue-600 font-medium text-sm">Attendance Percentage</p>
                <p className="text-3xl font-bold text-blue-900 mt-2">{percentage}%</p>
              </CardContent>
            </Card>
          </div>
        )}

        {/* Detailed History */}
        {selectedStudent && (
          <Card className="shadow-sm">
            <CardHeader>
              <CardTitle>Daily Records</CardTitle>
            </CardHeader>
            <CardContent>
              {attendance.length > 0 ? (
                <div className="overflow-x-auto">
                  <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                      <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Remarks</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Recorded By</th>
                      </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                      {attendance.map((record) => (
                        <tr key={record.id}>
                          <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{record.date}</td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm">
                            <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                              record.status === 'PRESENT' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                            }`}>
                              {record.status}
                            </span>
                          </td>
                          <td className="px-6 py-4 text-sm text-gray-500">{record.remarks || '-'}</td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{record.recordedByName}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <p className="text-gray-500 text-center py-8">No attendance records found.</p>
              )}
            </CardContent>
          </Card>
        )}

      </div>
    </div>
  )
}

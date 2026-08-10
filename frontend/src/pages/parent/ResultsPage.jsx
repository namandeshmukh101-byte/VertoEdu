import { useState, useEffect } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import api from '@/services/api'

export default function ResultsPage() {
  const [searchParams] = useSearchParams()
  const defaultStudentId = searchParams.get('studentId') || ''
  
  const [students, setStudents] = useState([])
  const [selectedStudent, setSelectedStudent] = useState(defaultStudentId)
  const [results, setResults] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchStudents = async () => {
      try {
        const res = await api.get('/parent/me/students')
        setStudents(res.data.data)
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
    const fetchResults = async () => {
      try {
        const res = await api.get(`/parent/students/${selectedStudent}/results`)
        setResults(res.data.data)
      } catch (err) {
        console.error('Error fetching results:', err)
      }
    }
    fetchResults()
  }, [selectedStudent])

  // Helper to calculate percentage and grade
  const getPerformance = (obtained, max) => {
    if (max === 0) return { percentage: 0, grade: 'N/A' }
    const pct = Math.round((obtained / max) * 100)
    let grade = 'F'
    if (pct >= 90) grade = 'A+'
    else if (pct >= 80) grade = 'A'
    else if (pct >= 70) grade = 'B'
    else if (pct >= 60) grade = 'C'
    else if (pct >= 50) grade = 'D'
    return { percentage: pct, grade }
  }

  if (loading) return <div className="p-8 text-center">Loading...</div>

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto space-y-6">
        
        <div className="flex justify-between items-center">
          <h1 className="text-3xl font-bold text-gray-900">Academic Results</h1>
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

        {/* Detailed Results */}
        {selectedStudent && (
          <Card className="shadow-sm">
            <CardHeader>
              <CardTitle>Exam Results</CardTitle>
            </CardHeader>
            <CardContent>
              {results.length > 0 ? (
                <div className="overflow-x-auto">
                  <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                      <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Exam</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Subject</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Marks</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">% / Grade</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Remarks</th>
                      </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                      {results.map((r) => {
                        const perf = getPerformance(r.marksObtained, r.maxMarks)
                        return (
                          <tr key={r.id}>
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{r.examName}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{r.subjectName}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{r.marksObtained} / {r.maxMarks}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm">
                              <span className="font-bold text-gray-700">{perf.percentage}%</span>
                              <span className={`ml-2 px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                                perf.grade === 'F' ? 'bg-red-100 text-red-800' : 'bg-green-100 text-green-800'
                              }`}>
                                {perf.grade}
                              </span>
                            </td>
                            <td className="px-6 py-4 text-sm text-gray-500">{r.remarks || '-'}</td>
                          </tr>
                        )
                      })}
                    </tbody>
                  </table>
                </div>
              ) : (
                <p className="text-gray-500 text-center py-8">No exam results published yet.</p>
              )}
            </CardContent>
          </Card>
        )}

      </div>
    </div>
  )
}

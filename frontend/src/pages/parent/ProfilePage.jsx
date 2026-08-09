import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import axios from 'axios'
import { useForm } from 'react-hook-form'
import { z } from 'zod'
import { zodResolver } from '@hookform/resolvers/zod'

const profileSchema = z.object({
  phone: z.string().min(10, 'Phone must be at least 10 characters'),
  alternateContact: z.string().optional(),
  address: z.string().min(5, 'Address is required')
})

export default function ProfilePage() {
  const [profile, setProfile] = useState(null)
  const [students, setStudents] = useState([])
  const [loading, setLoading] = useState(true)
  const [saveSuccess, setSaveSuccess] = useState(false)

  const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm({
    resolver: zodResolver(profileSchema)
  })

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [profileRes, studentsRes] = await Promise.all([
          axios.get('/api/parent/me'),
          axios.get('/api/parent/me/students')
        ])
        setProfile(profileRes.data.data)
        setStudents(studentsRes.data.data)
        reset(profileRes.data.data) // initialize form
      } catch (err) {
        console.error('Error fetching profile data:', err)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [reset])

  const onSubmit = async (data) => {
    try {
      setSaveSuccess(false)
      await axios.put('/api/parent/me', data)
      setSaveSuccess(true)
      setTimeout(() => setSaveSuccess(false), 3000)
    } catch (err) {
      console.error('Error updating profile:', err)
      alert('Failed to update profile')
    }
  }

  if (loading) return <div className="p-8 text-center">Loading...</div>

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-4xl mx-auto space-y-6">
        
        <div className="flex justify-between items-center">
          <h1 className="text-3xl font-bold text-gray-900">Parent Profile</h1>
          <Link to="/parent" className="text-indigo-600 hover:text-indigo-800 font-medium">
            &larr; Back to Dashboard
          </Link>
        </div>

        <Card className="shadow-sm">
          <CardHeader>
            <CardTitle>Personal Details</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-2 gap-4 mb-6 p-4 bg-gray-50 rounded-lg border border-gray-100">
              <div>
                <span className="block text-sm text-gray-500">Name</span>
                <span className="font-medium">{profile?.firstName} {profile?.lastName}</span>
              </div>
              <div>
                <span className="block text-sm text-gray-500">Email (Read Only)</span>
                <span className="font-medium text-gray-700">{profile?.email || 'N/A'}</span>
              </div>
            </div>

            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">Primary Contact Number</label>
                  <input 
                    type="text" 
                    {...register('phone')} 
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                  />
                  {errors.phone && <p className="text-red-600 text-sm mt-1">{errors.phone.message}</p>}
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">Alternate Contact (Optional)</label>
                  <input 
                    type="text" 
                    {...register('alternateContact')} 
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                  />
                  {errors.alternateContact && <p className="text-red-600 text-sm mt-1">{errors.alternateContact.message}</p>}
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Residential Address</label>
                <textarea 
                  {...register('address')} 
                  rows={3}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                />
                {errors.address && <p className="text-red-600 text-sm mt-1">{errors.address.message}</p>}
              </div>

              <div className="flex items-center space-x-4 pt-4">
                <button 
                  type="submit" 
                  disabled={isSubmitting}
                  className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 disabled:opacity-50 transition"
                >
                  {isSubmitting ? 'Saving...' : 'Save Changes'}
                </button>
                {saveSuccess && <span className="text-green-600 font-medium">Profile updated successfully!</span>}
              </div>
            </form>
          </CardContent>
        </Card>

        {/* Read Only Linked Students info */}
        <Card className="shadow-sm">
          <CardHeader>
            <CardTitle>Linked Students (Read Only)</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {students.map(s => (
                <div key={s.id} className="p-4 border rounded-md bg-gray-50 flex justify-between items-center">
                  <div>
                    <h3 className="font-bold text-gray-900">{s.firstName} {s.lastName}</h3>
                    <p className="text-sm text-gray-500">Roll No: {s.scholarNumber}</p>
                  </div>
                  <div className="text-right">
                    <p className="font-medium text-gray-700">{s.className}</p>
                    <p className="text-sm text-gray-500">Section {s.sectionName}</p>
                  </div>
                </div>
              ))}
              {students.length === 0 && <p className="text-gray-500 text-sm">No students linked.</p>}
              <p className="text-xs text-gray-400 mt-2">To modify academic records, please contact the school administration.</p>
            </div>
          </CardContent>
        </Card>

      </div>
    </div>
  )
}

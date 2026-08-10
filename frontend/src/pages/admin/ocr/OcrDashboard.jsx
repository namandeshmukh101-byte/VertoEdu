import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import Navbar from '@/components/Navbar'
import { Upload, FileText, CheckCircle, Clock, XCircle } from 'lucide-react'
import api from '@/services/api'

export default function OcrDashboard() {
  const [history, setHistory] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchHistory()
  }, [])

  const fetchHistory = async () => {
    try {
      const res = await api.get('/ocr/history')
      setHistory(res.data)
    } catch (err) {
      console.error('Failed to fetch history', err)
    } finally {
      setLoading(false)
    }
  }

  const getStatusBadge = (status) => {
    switch (status) {
      case 'APPROVED': return <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800"><CheckCircle className="w-3 h-3"/> Approved</span>
      case 'REJECTED': return <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800"><XCircle className="w-3 h-3"/> Rejected</span>
      case 'UPLOADED': return <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800"><Clock className="w-3 h-3"/> Pending OCR</span>
      default: return <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800"><Clock className="w-3 h-3"/> Processing</span>
    }
  }

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      
      <main className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-3xl font-bold text-foreground">OCR Intelligence</h1>
            <p className="mt-1 text-muted-foreground">Process documents and forms with AI assistance.</p>
          </div>
          <Link 
            to="/admin/ocr/upload" 
            className="inline-flex items-center gap-2 px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-colors shadow-sm"
          >
            <Upload className="w-4 h-4" />
            Upload Document
          </Link>
        </div>

        <div className="bg-card border rounded-xl shadow-sm overflow-hidden">
          <div className="px-6 py-4 border-b bg-muted/50">
            <h2 className="text-lg font-semibold flex items-center gap-2">
              <FileText className="w-5 h-5 text-muted-foreground" />
              Processing History
            </h2>
          </div>
          
          <div className="overflow-x-auto">
            <table className="w-full text-sm text-left">
              <thead className="text-xs text-muted-foreground uppercase bg-muted/20">
                <tr>
                  <th className="px-6 py-3 font-medium">Document ID</th>
                  <th className="px-6 py-3 font-medium">File Name</th>
                  <th className="px-6 py-3 font-medium">Type</th>
                  <th className="px-6 py-3 font-medium">Status</th>
                  <th className="px-6 py-3 font-medium">Date</th>
                  <th className="px-6 py-3 font-medium text-right">Action</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr><td colSpan="6" className="px-6 py-8 text-center text-muted-foreground">Loading history...</td></tr>
                ) : history.length === 0 ? (
                  <tr><td colSpan="6" className="px-6 py-8 text-center text-muted-foreground">No documents processed yet.</td></tr>
                ) : (
                  history.map((doc) => (
                    <tr key={doc.id} className="border-b last:border-0 hover:bg-muted/10 transition-colors">
                      <td className="px-6 py-4 font-medium">#{doc.id}</td>
                      <td className="px-6 py-4">{doc.fileName}</td>
                      <td className="px-6 py-4 text-muted-foreground">{doc.documentType.replace('_', ' ')}</td>
                      <td className="px-6 py-4">{getStatusBadge(doc.status)}</td>
                      <td className="px-6 py-4 text-muted-foreground">{new Date(doc.uploadedAt).toLocaleDateString()}</td>
                      <td className="px-6 py-4 text-right">
                        {(doc.status === 'UPLOADED' || doc.status === 'OCR_COMPLETED' || doc.status === 'AI_COMPLETED') ? (
                          <Link to={`/admin/ocr/process/${doc.id}`} className="text-primary hover:underline font-medium">
                            Continue Review
                          </Link>
                        ) : (
                          <span className="text-muted-foreground cursor-not-allowed">Completed</span>
                        )}
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </main>
    </div>
  )
}

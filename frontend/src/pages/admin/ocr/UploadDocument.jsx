import { useState, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import Navbar from '@/components/Navbar'
import { UploadCloud, File, AlertCircle, CheckCircle2 } from 'lucide-react'
import api from '@/services/api'

export default function UploadDocument() {
  const navigate = useNavigate()
  const [file, setFile] = useState(null)
  const [docType, setDocType] = useState('ADMISSION_FORM')
  const [uploading, setUploading] = useState(false)
  const [error, setError] = useState('')
  const [dragActive, setDragActive] = useState(false)

  const handleDrag = (e) => {
    e.preventDefault()
    e.stopPropagation()
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true)
    } else if (e.type === 'dragleave') {
      setDragActive(false)
    }
  }

  const handleDrop = (e) => {
    e.preventDefault()
    e.stopPropagation()
    setDragActive(false)
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      setFile(e.dataTransfer.files[0])
    }
  }

  const handleChange = (e) => {
    e.preventDefault()
    if (e.target.files && e.target.files[0]) {
      setFile(e.target.files[0])
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!file) {
      setError('Please select a file to upload.')
      return
    }

    if (file.size > 5 * 1024 * 1024) {
      setError('File size must be under 5MB.')
      return
    }

    const formData = new FormData()
    formData.append('file', file)
    formData.append('type', docType)

    setUploading(true)
    setError('')

    try {
      const res = await api.post('/ocr/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      navigate(`/admin/ocr/process/${res.data.id}`)
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to upload document.')
      setUploading(false)
    }
  }

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <Navbar />
      
      <main className="flex-1 flex flex-col items-center justify-center p-4">
        <div className="w-full max-w-xl bg-card border rounded-2xl shadow-lg p-8">
          <div className="text-center mb-8">
            <h1 className="text-2xl font-bold text-foreground">Upload Document</h1>
            <p className="text-muted-foreground mt-2">Upload a student document for OCR data extraction.</p>
          </div>

          {error && (
            <div className="mb-6 p-4 rounded-lg bg-red-50 text-red-700 flex items-start gap-3">
              <AlertCircle className="w-5 h-5 mt-0.5 shrink-0" />
              <p className="text-sm font-medium">{error}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            
            <div>
              <label className="block text-sm font-medium mb-2">Document Type</label>
              <select 
                value={docType}
                onChange={(e) => setDocType(e.target.value)}
                className="w-full p-3 border rounded-lg bg-background text-foreground focus:ring-2 focus:ring-primary focus:border-transparent outline-none transition-all"
              >
                <option value="ADMISSION_FORM">Admission Form</option>
                <option value="MARK_SHEET">Mark Sheet</option>
                <option value="TRANSFER_CERTIFICATE">Transfer Certificate</option>
                <option value="BIRTH_CERTIFICATE">Birth Certificate</option>
              </select>
            </div>

            <div 
              onDragEnter={handleDrag}
              onDragLeave={handleDrag}
              onDragOver={handleDrag}
              onDrop={handleDrop}
              className={`relative border-2 border-dashed rounded-xl p-10 text-center transition-all ${
                dragActive ? 'border-primary bg-primary/5' : 'border-border hover:bg-muted/30'
              }`}
            >
              <input
                type="file"
                id="file-upload"
                className="hidden"
                onChange={handleChange}
                accept="image/*,.pdf"
              />
              
              {!file ? (
                <label htmlFor="file-upload" className="cursor-pointer flex flex-col items-center gap-4">
                  <div className="w-16 h-16 rounded-full bg-primary/10 flex items-center justify-center">
                    <UploadCloud className="w-8 h-8 text-primary" />
                  </div>
                  <div>
                    <span className="text-primary font-medium">Click to upload</span> or drag and drop
                    <p className="text-xs text-muted-foreground mt-1">PNG, JPG, or PDF (max. 5MB)</p>
                  </div>
                </label>
              ) : (
                <div className="flex flex-col items-center gap-4">
                  <div className="w-16 h-16 rounded-full bg-green-100 flex items-center justify-center">
                    <CheckCircle2 className="w-8 h-8 text-green-600" />
                  </div>
                  <div className="text-center">
                    <p className="font-medium text-foreground flex items-center justify-center gap-2">
                      <File className="w-4 h-4 text-muted-foreground" />
                      {file.name}
                    </p>
                    <button 
                      type="button"
                      onClick={(e) => { e.preventDefault(); setFile(null); }}
                      className="text-sm text-red-500 hover:text-red-700 mt-2"
                    >
                      Remove file
                    </button>
                  </div>
                </div>
              )}
            </div>

            <button
              type="submit"
              disabled={!file || uploading}
              className="w-full py-3 px-4 bg-primary text-primary-foreground font-medium rounded-lg hover:bg-primary/90 transition-colors disabled:opacity-50 flex items-center justify-center gap-2"
            >
              {uploading ? (
                <>
                  <div className="w-5 h-5 border-2 border-primary-foreground/30 border-t-primary-foreground rounded-full animate-spin" />
                  Uploading...
                </>
              ) : (
                'Upload and Proceed to OCR'
              )}
            </button>
          </form>
        </div>
      </main>
    </div>
  )
}

import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import Navbar from '@/components/Navbar'
import { FileText, Cpu, CheckSquare, AlertTriangle, CheckCircle, Save, XCircle, ArrowLeft } from 'lucide-react'
import { Link } from 'react-router-dom'
import api from '@/services/api'

export default function ProcessingPipeline() {
  const { id } = useParams()
  const navigate = useNavigate()
  
  const [step, setStep] = useState(1) // 1: OCR, 2: AI, 3: Approval
  const [loading, setLoading] = useState(true)
  const [ocrData, setOcrData] = useState(null)
  const [aiData, setAiData] = useState(null)
  
  // Editable form state
  const [formData, setFormData] = useState({})
  const [rejectionReason, setRejectionReason] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState('')
  const [aiUnavailable, setAiUnavailable] = useState(false)

  useEffect(() => {
    startPipeline()
  }, [id])

  const startPipeline = async () => {
    setLoading(true)
    setError('')
    try {
      // Step 1: Trigger OCR Processing
      const ocrRes = await api.post(`/ocr/process/${id}`)
      setOcrData(ocrRes.data)
      setFormData(ocrRes.data.extractedData || {})
      setStep(2)
      
      // Step 2: Trigger AI Review
      try {
        const aiRes = await api.post(`/ai/suggest/${id}`)
        setAiData(aiRes.data)
        setStep(3)
        
        // Automatically apply AI suggestions to form data for convenience
        if (aiRes.data.suggestedData) {
          setFormData(aiRes.data.suggestedData)
        }
      } catch (aiErr) {
        if (aiErr.response?.status === 429) {
          setAiUnavailable(true)
          setStep(3) // Allow manual verification
        } else {
          throw aiErr // Rethrow other errors
        }
      }
      
    } catch (err) {
      console.error(err)
      setError(err.response?.data?.message || 'Processing failed. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  const handleApproval = async (isApproved) => {
    if (!isApproved && !rejectionReason.trim()) {
      setError('Please provide a reason for rejection.')
      return
    }

    setSubmitting(true)
    setError('')
    try {
      await api.post('/approval/submit', {
        documentUploadId: parseInt(id),
        isApproved,
        finalApprovedData: isApproved ? formData : null,
        rejectionReason: isApproved ? null : rejectionReason
      })
      
      navigate('/admin/ocr')
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit decision.')
      setSubmitting(false)
    }
  }

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <Navbar />
      
      <main className="flex-1 flex flex-col mx-auto w-full max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div className="mb-4">
          <Link to="/admin/ocr" className="inline-flex items-center gap-2 text-sm text-muted-foreground hover:text-foreground transition-colors">
            <ArrowLeft className="w-4 h-4" />
            Back to OCR Dashboard
          </Link>
        </div>
        <div className="mb-6 flex justify-between items-end">
          <div>
            <h1 className="text-2xl font-bold text-foreground">Document Processing Pipeline</h1>
            <p className="text-muted-foreground mt-1">Review OCR extraction and AI suggestions before approval.</p>
          </div>
          <div className="flex gap-4 items-center">
            <div className={`flex items-center gap-2 ${step >= 1 ? 'text-primary' : 'text-muted-foreground'}`}>
              <div className={`w-8 h-8 rounded-full flex items-center justify-center ${step >= 1 ? 'bg-primary/20' : 'bg-muted'}`}>1</div>
              <span className="font-medium">OCR</span>
            </div>
            <div className="w-10 h-px bg-border"></div>
            <div className={`flex items-center gap-2 ${step >= 2 ? 'text-primary' : 'text-muted-foreground'}`}>
              <div className={`w-8 h-8 rounded-full flex items-center justify-center ${step >= 2 ? 'bg-primary/20' : 'bg-muted'}`}>2</div>
              <span className="font-medium">AI Review</span>
            </div>
            <div className="w-10 h-px bg-border"></div>
            <div className={`flex items-center gap-2 ${step >= 3 ? 'text-primary' : 'text-muted-foreground'}`}>
              <div className={`w-8 h-8 rounded-full flex items-center justify-center ${step >= 3 ? 'bg-primary/20' : 'bg-muted'}`}>3</div>
              <span className="font-medium">Approval</span>
            </div>
          </div>
        </div>

        {error && (
          <div className="mb-6 p-4 bg-red-50 text-red-700 rounded-lg flex items-start gap-3">
            <AlertTriangle className="w-5 h-5 shrink-0" />
            <p>{error}</p>
          </div>
        )}

        {aiUnavailable && (
          <div className="mb-6 p-4 bg-yellow-50 text-yellow-800 rounded-lg flex items-start gap-3 border border-yellow-200 shadow-sm">
            <AlertTriangle className="w-5 h-5 shrink-0 text-yellow-600" />
            <div>
              <p className="font-semibold text-yellow-900">AI REVIEW: UNAVAILABLE</p>
              <p className="text-sm mt-1">AI review is temporarily unavailable due to API quota. MANUAL VERIFICATION: REQUIRED.</p>
            </div>
          </div>
        )}

        {loading ? (
          <div className="flex-1 flex flex-col items-center justify-center bg-card border rounded-2xl p-12">
            <div className="relative w-24 h-24 mb-6">
              <div className="absolute inset-0 border-4 border-muted rounded-full"></div>
              <div className="absolute inset-0 border-4 border-primary border-t-transparent rounded-full animate-spin"></div>
              <div className="absolute inset-0 flex items-center justify-center">
                {step === 1 ? <FileText className="w-8 h-8 text-primary animate-pulse" /> : <Cpu className="w-8 h-8 text-primary animate-pulse" />}
              </div>
            </div>
            <h3 className="text-xl font-semibold text-foreground">
              {step === 1 ? 'Extracting text via OCR Engine...' : 'Analyzing data with OpenAI...'}
            </h3>
            <p className="text-muted-foreground mt-2 max-w-md text-center">
              Please wait while our intelligent systems process your document. This may take a few moments.
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 flex-1">
            
            {/* Column 1: Raw OCR & AI Suggestions */}
            <div className="lg:col-span-1 space-y-6 flex flex-col">
              <div className="bg-card border rounded-xl overflow-hidden flex-1 flex flex-col">
                <div className="px-4 py-3 bg-muted/30 border-b font-medium flex items-center gap-2">
                  <FileText className="w-4 h-4 text-muted-foreground" /> Raw OCR Text
                </div>
                <div className="p-4 bg-gray-50/50 flex-1 overflow-auto text-sm font-mono whitespace-pre-wrap text-muted-foreground">
                  {ocrData?.rawText}
                </div>
              </div>

              <div className={`border rounded-xl overflow-hidden flex-1 flex flex-col ${aiUnavailable ? 'bg-gray-50 border-gray-200' : 'bg-blue-50/50 border-blue-100'}`}>
                <div className={`px-4 py-3 border-b font-medium flex items-center gap-2 ${aiUnavailable ? 'bg-gray-100 text-gray-700 border-gray-200' : 'bg-blue-100/50 text-blue-800 border-blue-100'}`}>
                  <Cpu className="w-4 h-4" /> AI Suggestions
                </div>
                <div className={`p-4 flex-1 overflow-auto text-sm whitespace-pre-wrap ${aiUnavailable ? 'text-gray-500 italic' : 'text-blue-900'}`}>
                  {aiUnavailable ? "AI suggestions are not available. Please verify manually using the raw OCR text above." : (aiData?.suggestionsText || "No suggestions provided.")}
                </div>
              </div>
            </div>

            {/* Column 2: Editable Form & Approval */}
            <div className="lg:col-span-2 bg-card border rounded-xl overflow-hidden flex flex-col">
              <div className="px-6 py-4 bg-muted/30 border-b font-semibold flex items-center gap-2">
                <CheckSquare className="w-5 h-5 text-muted-foreground" /> Final Verification Form
              </div>
              
              <div className="p-6 flex-1 overflow-auto">
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-6 gap-y-4">
                  {Object.entries(formData).map(([key, value]) => (
                    <div key={key} className="space-y-1">
                      <label className="text-sm font-medium text-muted-foreground capitalize">
                        {key.replace(/([A-Z])/g, ' $1').trim()}
                      </label>
                      <input
                        type="text"
                        name={key}
                        value={value || ''}
                        onChange={handleInputChange}
                        className="w-full p-2.5 bg-background border rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent outline-none"
                      />
                    </div>
                  ))}
                </div>
              </div>

              <div className="p-6 bg-muted/20 border-t space-y-4">
                <div>
                  <label className="text-sm font-medium text-muted-foreground mb-1 block">Rejection Reason (Optional)</label>
                  <input
                    type="text"
                    value={rejectionReason}
                    onChange={(e) => setRejectionReason(e.target.value)}
                    placeholder="If rejecting, specify why..."
                    className="w-full p-2.5 bg-background border rounded-lg outline-none"
                  />
                </div>

                <div className="flex gap-4">
                  <button
                    onClick={() => handleApproval(false)}
                    disabled={submitting}
                    className="flex-1 py-3 px-4 bg-white border border-red-200 text-red-600 hover:bg-red-50 font-medium rounded-lg transition-colors flex items-center justify-center gap-2 disabled:opacity-50"
                  >
                    <XCircle className="w-5 h-5" /> Reject
                  </button>
                  
                  <button
                    onClick={() => handleApproval(true)}
                    disabled={submitting}
                    className="flex-[2] py-3 px-4 bg-green-600 hover:bg-green-700 text-white font-medium rounded-lg transition-colors flex items-center justify-center gap-2 shadow-sm disabled:opacity-50"
                  >
                    {submitting ? (
                      <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                    ) : (
                      <><CheckCircle className="w-5 h-5" /> Approve & Save to Database</>
                    )}
                  </button>
                </div>
              </div>

            </div>
          </div>
        )}
      </main>
    </div>
  )
}

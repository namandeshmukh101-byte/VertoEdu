import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { Link } from 'react-router-dom'
import { motion, AnimatePresence } from 'framer-motion'

export default function CookieConsent() {
  const [isVisible, setIsVisible] = useState(false)

  useEffect(() => {
    const preferences = localStorage.getItem('vertoedu_cookie_preferences')
    if (!preferences) {
      setIsVisible(true)
    }
  }, [])

  const handleAcceptAll = () => {
    localStorage.setItem('vertoedu_cookie_preferences', JSON.stringify({ essential: true, analytics: true, performance: true }))
    setIsVisible(false)
  }

  const handleRejectNonEssential = () => {
    localStorage.setItem('vertoedu_cookie_preferences', JSON.stringify({ essential: true, analytics: false, performance: false }))
    setIsVisible(false)
  }

  return (
    <AnimatePresence>
      {isVisible && (
        <motion.div
          initial={{ y: 100, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          exit={{ y: 100, opacity: 0 }}
          className="fixed bottom-0 left-0 right-0 z-[100] p-4 pointer-events-none"
        >
          <div className="mx-auto max-w-4xl bg-card border border-border shadow-2xl rounded-2xl p-6 pointer-events-auto flex flex-col md:flex-row items-center gap-6 justify-between">
            <div className="flex-1">
              <h3 className="text-lg font-semibold text-foreground mb-2">We value your privacy</h3>
              <p className="text-sm text-muted-foreground leading-relaxed">
                We use cookies to enhance your browsing experience, serve personalized content, and analyze our traffic. By clicking "Accept All", you consent to our use of cookies. 
                Read our <Link to="/cookies" className="text-primary hover:underline">Cookie Policy</Link> for more details.
              </p>
            </div>
            <div className="flex flex-col sm:flex-row gap-3 w-full md:w-auto shrink-0">
              <Button variant="outline" onClick={handleRejectNonEssential}>
                Reject Non-Essential
              </Button>
              <Button onClick={handleAcceptAll}>
                Accept All
              </Button>
            </div>
          </div>
        </motion.div>
      )}
    </AnimatePresence>
  )
}

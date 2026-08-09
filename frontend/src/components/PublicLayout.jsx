import PublicNavbar from '@/components/PublicNavbar'
import Footer from '@/components/Footer'
import CookieConsent from '@/components/CookieConsent'

export default function PublicLayout({ children }) {
  return (
    <div className="flex flex-col min-h-screen">
      <PublicNavbar />
      <main className="flex-grow">
        {children}
      </main>
      <Footer />
      <CookieConsent />
    </div>
  )
}

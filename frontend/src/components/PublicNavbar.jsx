import { Link } from 'react-router-dom'
import { GraduationCap, Sparkles, Menu, X } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { useState } from 'react'

export default function PublicNavbar() {
  const [isOpen, setIsOpen] = useState(false)

  return (
    <nav className="sticky top-0 z-50 w-full border-b border-border/50 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="container mx-auto flex h-16 max-w-6xl items-center justify-between px-4">
        {/* Logo */}
        <Link to="/" className="flex items-center gap-2 transition-opacity hover:opacity-80">
          <div className="relative">
            <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-primary/10">
              <GraduationCap className="h-6 w-6 text-primary" />
            </div>
            <div className="absolute -right-1 -top-1 flex h-4 w-4 items-center justify-center rounded-full bg-primary">
              <Sparkles className="h-2 w-2 text-white" />
            </div>
          </div>
          <span className="text-xl font-bold tracking-tight text-foreground">
            Verto<span className="text-primary">Edu</span>
          </span>
        </Link>

        {/* Desktop Navigation */}
        <div className="hidden md:flex items-center gap-6 text-sm font-medium text-muted-foreground">
          <Link to="/" className="hover:text-primary transition-colors">Home</Link>
          <Link to="/about" className="hover:text-primary transition-colors">About</Link>
          <Link to="/faq" className="hover:text-primary transition-colors">FAQ</Link>
          <Link to="/contact" className="hover:text-primary transition-colors">Contact</Link>
        </div>

        {/* Actions */}
        <div className="hidden md:flex items-center gap-4">
          <Button variant="ghost" asChild>
            <Link to="/login">Login</Link>
          </Button>
          <Button asChild>
            <Link to="/login">Get Started</Link>
          </Button>
        </div>

        {/* Mobile menu button */}
        <button 
          className="md:hidden flex items-center justify-center w-10 h-10 text-foreground"
          onClick={() => setIsOpen(!isOpen)}
        >
          {isOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
        </button>
      </div>

      {/* Mobile Navigation */}
      {isOpen && (
        <div className="md:hidden border-t border-border/50 bg-background px-4 py-6 flex flex-col gap-4 shadow-lg">
          <Link to="/" className="text-base font-medium" onClick={() => setIsOpen(false)}>Home</Link>
          <Link to="/about" className="text-base font-medium" onClick={() => setIsOpen(false)}>About</Link>
          <Link to="/faq" className="text-base font-medium" onClick={() => setIsOpen(false)}>FAQ</Link>
          <Link to="/contact" className="text-base font-medium" onClick={() => setIsOpen(false)}>Contact</Link>
          <div className="h-px bg-border/50 my-2" />
          <Button variant="outline" className="w-full justify-center" asChild onClick={() => setIsOpen(false)}>
            <Link to="/login">Login</Link>
          </Button>
          <Button className="w-full justify-center" asChild onClick={() => setIsOpen(false)}>
            <Link to="/login">Get Started</Link>
          </Button>
        </div>
      )}
    </nav>
  )
}

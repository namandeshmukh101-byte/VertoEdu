import { Link } from 'react-router-dom'
import { motion } from 'framer-motion'
import { GraduationCap, Sparkles, ArrowRight, ShieldCheck, Zap, BarChart3, ScanText, BrainCircuit, Users } from 'lucide-react'
import { Button } from '@/components/ui/button'

const features = [
  {
    title: "AI-Powered Admissions",
    description: "Automate document processing with advanced OCR and OpenAI integration.",
    icon: ScanText,
  },
  {
    title: "Smart Insights",
    description: "Real-time analytics for attendance, performance, and school operations.",
    icon: BarChart3,
  },
  {
    title: "Role-Based Dashboards",
    description: "Dedicated experiences for Administrators, Teachers, and Parents.",
    icon: Users,
  },
  {
    title: "Enterprise Security",
    description: "Bank-grade security with Spring Security and Google OAuth2 integration.",
    icon: ShieldCheck,
  },
  {
    title: "Lightning Fast",
    description: "Optimized React frontend and Spring Boot backend for maximum performance.",
    icon: Zap,
  },
  {
    title: "Responsible AI",
    description: "Human-in-the-loop approval processes ensure AI assists rather than decides.",
    icon: BrainCircuit,
  }
]

export default function LandingPage() {
  return (
    <div className="min-h-screen flex flex-col bg-background overflow-x-hidden">
      {/* Hero Section */}
      <section className="relative pt-32 pb-20 lg:pt-48 lg:pb-32 px-4">
        {/* Animated background blobs */}
        <div className="absolute top-0 left-0 w-full h-full overflow-hidden -z-10">
          <div className="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] rounded-full bg-primary/5 blur-[100px]" />
          <div className="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] rounded-full bg-primary/10 blur-[120px]" />
        </div>

        <div className="container mx-auto max-w-6xl text-center">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
            className="flex flex-col items-center gap-6"
          >
            <div className="relative inline-flex mb-4">
              <div className="w-24 h-24 rounded-3xl bg-primary/10 flex items-center justify-center border border-primary/20 shadow-sm shadow-primary/10">
                <GraduationCap className="w-12 h-12 text-primary" />
              </div>
              <motion.div 
                animate={{ rotate: 360 }} 
                transition={{ duration: 20, repeat: Infinity, ease: "linear" }}
                className="absolute -top-2 -right-2 w-8 h-8 rounded-full bg-primary flex items-center justify-center shadow-lg"
              >
                <Sparkles className="w-4 h-4 text-white" />
              </motion.div>
            </div>

            <h1 className="text-5xl sm:text-6xl lg:text-7xl font-extrabold tracking-tight text-foreground max-w-4xl">
              Verto<span className="text-primary">Edu</span>
            </h1>
            
            <p className="text-xl sm:text-2xl font-medium text-foreground max-w-2xl mt-2">
              Where <span className="text-primary font-semibold">AI</span> Meets Education
            </p>

            <p className="text-lg text-muted-foreground max-w-2xl leading-relaxed mt-4">
              A next-generation School Operations Platform designed to automate administration, 
              empower teachers, and engage parents.
            </p>

            <div className="flex flex-col sm:flex-row gap-4 mt-8 w-full sm:w-auto">
              <Button size="lg" className="gap-2 h-14 px-8 text-base shadow-lg shadow-primary/25" asChild>
                <Link to="/login">
                  Get Started <ArrowRight className="w-5 h-5" />
                </Link>
              </Button>
              <Button size="lg" variant="outline" className="gap-2 h-14 px-8 text-base bg-white" asChild>
                <Link to="/about">
                  Learn More
                </Link>
              </Button>
            </div>
          </motion.div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-24 bg-white px-4 border-t border-border/50">
        <div className="container mx-auto max-w-6xl">
          <div className="text-center mb-16">
            <h2 className="text-3xl font-bold tracking-tight text-foreground sm:text-4xl">
              Everything you need to run a modern school
            </h2>
            <p className="mt-4 text-lg text-muted-foreground max-w-2xl mx-auto">
              VertoEdu combines the reliability of a traditional ERP with the power of artificial intelligence.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {features.map((feature, idx) => (
              <motion.div
                key={idx}
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.5, delay: idx * 0.1 }}
                className="bg-background rounded-2xl p-8 border border-border/50 shadow-sm hover:shadow-md transition-shadow"
              >
                <div className="w-12 h-12 rounded-lg bg-primary/10 flex items-center justify-center mb-6">
                  <feature.icon className="w-6 h-6 text-primary" />
                </div>
                <h3 className="text-xl font-semibold mb-3 text-foreground">{feature.title}</h3>
                <p className="text-muted-foreground leading-relaxed">{feature.description}</p>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* Call to action */}
      <section className="py-24 px-4 bg-primary text-white">
        <div className="container mx-auto max-w-4xl text-center">
          <h2 className="text-3xl sm:text-4xl font-bold tracking-tight mb-6">
            Ready to transform your school?
          </h2>
          <p className="text-primary-foreground/80 text-lg mb-10 max-w-2xl mx-auto">
            Join the educational institutions adopting VertoEdu to streamline operations and focus on what matters most: student success.
          </p>
          <Button size="lg" variant="secondary" className="gap-2 h-14 px-8 text-base w-full sm:w-auto text-primary font-semibold hover:bg-white/90" asChild>
            <Link to="/login">
              Access Dashboard <ArrowRight className="w-5 h-5" />
            </Link>
          </Button>
        </div>
      </section>
    </div>
  )
}

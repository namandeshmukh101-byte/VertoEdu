import { Helmet } from 'react-helmet-async'
import { User } from 'lucide-react'

export default function AboutDeveloper() {
  return (
    <div className="container mx-auto max-w-4xl px-4 py-16">
      <Helmet>
        <title>About the Developer | VertoEdu</title>
      </Helmet>
      
      <div className="bg-background rounded-2xl p-8 border border-border shadow-sm">
        <h1 className="text-3xl font-bold tracking-tight mb-8">About the Developer</h1>
        
        <div className="flex flex-col md:flex-row gap-10 items-start">
          {/* Photo Placeholder */}
          <div className="shrink-0">
            <div className="w-48 h-48 rounded-full bg-primary/10 border-4 border-background shadow-md overflow-hidden flex items-center justify-center">
              {/* This can be replaced with an actual <img /> later */}
              <User className="w-20 h-20 text-primary/40" />
            </div>
          </div>
          
          {/* Content Placeholder */}
          <div className="flex-1 space-y-6">
            <div>
              <h2 className="text-2xl font-bold text-foreground">
                [Developer Name]
              </h2>
              <p className="text-lg text-primary font-medium mt-1">
                [Developer Title / Role]
              </p>
            </div>
            
            <div className="prose prose-slate max-w-none text-muted-foreground leading-relaxed">
              <p>
                [This is a placeholder for the final About Me paragraph. The final text will be inserted here without requiring any redesign to the surrounding layout or database updates.]
              </p>
              <p>
                [Additional biography details or mission statements can be placed here.]
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

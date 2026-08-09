import { Helmet } from 'react-helmet-async'

export default function Security() {
  return (
    <div className="container mx-auto max-w-4xl px-4 py-16">
      <Helmet>
        <title>Security | VertoEdu</title>
      </Helmet>
      
      <div className="prose prose-slate max-w-none">
        <h1 className="text-4xl font-bold tracking-tight mb-8">Security</h1>
        
        <div className="bg-muted/50 rounded-xl p-8 border border-border">
          <p className="text-muted-foreground italic">
            This is a placeholder page for the Security.
            The school administration will provide the official legal and informational content prior to production deployment.
          </p>
        </div>
      </div>
    </div>
  )
}

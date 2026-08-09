const fs = require('fs');
const path = require('path');

const pages = [
  { name: 'PrivacyPolicy', title: 'Privacy Policy' },
  { name: 'Terms', title: 'Terms & Conditions' },
  { name: 'Cookies', title: 'Cookie Policy' },
  { name: 'About', title: 'About Us' },
  { name: 'Contact', title: 'Contact Us' },
  { name: 'FAQ', title: 'Frequently Asked Questions' },
  { name: 'Accessibility', title: 'Accessibility Statement' },
  { name: 'ResponsibleAI', title: 'Responsible AI' },
  { name: 'Security', title: 'Security' }
];

const template = (name, title) => `import { Helmet } from 'react-helmet-async'

export default function ${name}() {
  return (
    <div className="container mx-auto max-w-4xl px-4 py-16">
      <Helmet>
        <title>${title} | VertoEdu</title>
      </Helmet>
      
      <div className="prose prose-slate max-w-none">
        <h1 className="text-4xl font-bold tracking-tight mb-8">${title}</h1>
        
        <div className="bg-muted/50 rounded-xl p-8 border border-border">
          <p className="text-muted-foreground italic">
            This is a placeholder page for the ${title}.
            The school administration will provide the official legal and informational content prior to production deployment.
          </p>
        </div>
      </div>
    </div>
  )
}
`;

const dir = path.join(__dirname, '..', 'public-pages');
if (!fs.existsSync(dir)) {
  fs.mkdirSync(dir, { recursive: true });
}

pages.forEach(p => {
  fs.writeFileSync(path.join(dir, `${p.name}.jsx`), template(p.name, p.title));
});
console.log('Pages created successfully.');

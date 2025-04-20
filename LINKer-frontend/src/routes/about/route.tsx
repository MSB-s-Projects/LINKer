import { createFileRoute, Link } from '@tanstack/react-router'
import { Info, BarChart, Link as LinkLogo, Globe, Users } from 'lucide-react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { H2, P } from '@/components/ui/typography'

export const Route = createFileRoute('/about')({
	component: About,
})

function About() {
	return (
		<div className='flex flex-col items-center justify-center min-h-screen p-6 bg-background text-foreground'>
			<Card className='max-w-6xl shadow-md rounded-lg'>
				<CardHeader>
					<div className='flex items-center space-x-4'>
						<Info className='w-8 h-8 text-primary' />
						<CardTitle className='text-3xl font-bold'>About LINKer</CardTitle>
					</div>
				</CardHeader>
				<CardContent>
					<P className='mb-6 leading-relaxed'>
						LINKer is a cutting-edge URL shortening application designed to simplify the way you share links. Whether
						you're a business professional, a marketer, or just someone who loves sharing content, LINKer empowers you
						to create short, memorable links for your long URLs. With LINKer, sharing links becomes effortless,
						professional, and efficient.
					</P>

					{/* Features Section */}
					<div className='mb-8'>
						<H2 className='text-2xl font-semibold mb-4'>Key Features</H2>
						<ul className='space-y-4'>
							<li className='flex items-start space-x-3'>
								<LinkLogo className='w-6 h-6 text-primary' />
								<span>
									<strong>Custom Short Links:</strong> Create personalized short links that are easy to remember and
									share.
								</span>
							</li>
							<li className='flex items-start space-x-3'>
								<BarChart className='w-6 h-6 text-primary' />
								<span>
									<strong>Advanced Analytics:</strong> Gain insights into your audience with detailed analytics,
									including click counts, geographic data, and device information.
								</span>
							</li>
							<li className='flex items-start space-x-3'>
								<Globe className='w-6 h-6 text-primary' />
								<span>
									<strong>Global Reach:</strong> Share your links with a global audience and track their performance
									across different regions.
								</span>
							</li>
							<li className='flex items-start space-x-3'>
								<Users className='w-6 h-6 text-primary' />
								<span>
									<strong>Team Collaboration:</strong> Collaborate with your team to manage and analyze links
									effectively.
								</span>
							</li>
						</ul>
					</div>

					{/* Why Choose LINKer Section */}
					<div className='mb-8'>
						<H2 className='text-2xl font-semibold mb-4'>Why Choose LINKer?</H2>
						<P className='leading-relaxed mb-4'>
							LINKer is more than just a URL shortener. It's a comprehensive tool designed to help you maximize the
							impact of your links. Here's why LINKer stands out:
						</P>
						<ul className='list-disc list-inside space-y-2'>
							<li>Easy-to-use interface for creating and managing links.</li>
							<li>Real-time analytics to track link performance.</li>
							<li>Custom branding options to match your business identity.</li>
							<li>Secure and reliable infrastructure to ensure your links are always accessible.</li>
						</ul>
					</div>

					{/* How It Works Section */}
					<div className='mb-8'>
						<H2 className='text-2xl font-semibold mb-4'>How It Works</H2>
						<P className='leading-relaxed'>Using LINKer is simple and straightforward:</P>
						<ol className='list-decimal list-inside space-y-2 mt-4'>
							<li>Paste your long URL into the LINKer input field.</li>
							<li>Customize your short link (optional).</li>
							<li>Click "Shorten" to generate your short link.</li>
							<li>Share your link and track its performance in real-time.</li>
						</ol>
					</div>

					{/* Call to Action Section */}
					<div className='p-6 rounded-lg shadow-inner bg-muted text-muted-foreground'>
						<H2 className='text-xl font-semibold mb-2'>Start Using LINKer Today!</H2>
						<P className='mb-4'>
							Join thousands of users who trust LINKer to simplify their link-sharing experience. Whether you're an
							individual or a business, LINKer has the tools you need to succeed.
						</P>
						<Link to='/'>
							<Button className='bg-primary text-primary-foreground'>Get Started</Button>
						</Link>
					</div>
				</CardContent>
			</Card>
		</div>
	)
}


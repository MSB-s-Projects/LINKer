import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { createFileRoute } from '@tanstack/react-router'
import { Link } from 'lucide-react'
import { Features } from './components/features.lazy'
import { Hero } from './components/hero.lazy'

export const Route = createFileRoute('/')({
	component: Index,
})

function Index() {
	return (
		<div className='flex flex-col items-center justify-center min-h-screen p-6 bg-background text-foreground'>
			<Card className='max-w-6xl shadow-md rounded-lg'>
				<CardHeader>
					<div className='flex space-x-4'>
						<Link className='w-8 h-8 text-primary' />
						<CardTitle className='text-3xl font-bold'>
							LINKer Simplifies URL Shortening For Efficient Sharing.
						</CardTitle>
					</div>
				</CardHeader>
				<CardContent>
					{/* Hero Section */}
					<Hero />

					{/* Features Section */}
					<Features />
				</CardContent>
			</Card>
		</div>
	)
}


import { Card, CardContent } from '@/components/ui/card'
import { Shield, Shuffle, TrendingUp } from 'lucide-react'

export const Features = () => (
	<section id='features' className='w-full py-12 md:py-24 lg:py-32 bg-muted/50'>
		<div className='container px-4 md:px-6'>
			<div className='flex flex-col items-center justify-center space-y-4 text-center'>
				<div className='space-y-2'>
					<h2 className='text-3xl font-bold tracking-tighter md:text-4xl/tight'>
						Powerful Features for Link Management
					</h2>
					<p className='max-w-[900px] text-muted-foreground md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed'>
						Everything you need to create, manage, and analyze your shortened URLs.
					</p>
				</div>
			</div>
			<div className='mx-auto grid max-w-5xl items-center gap-6 py-12 lg:grid-cols-3'>
				<Card>
					<CardContent className='pt-6'>
						<div className='flex flex-col items-center space-y-2 text-center'>
							<div className='flex h-12 w-12 items-center justify-center rounded-full bg-primary/10'>
								<Shuffle className='h-6 w-6 text-primary' />
							</div>
							<h3 className='text-xl font-bold'>Custom Short Links</h3>
							<p className='text-muted-foreground'>Create branded, memorable links that reflect your identity.</p>
						</div>
					</CardContent>
				</Card>
				<Card>
					<CardContent className='pt-6'>
						<div className='flex flex-col items-center space-y-2 text-center'>
							<div className='flex h-12 w-12 items-center justify-center rounded-full bg-primary/10'>
								<TrendingUp className='h-6 w-6 text-primary' />
							</div>
							<h3 className='text-xl font-bold'>Real-time Analytics</h3>
							<p className='text-muted-foreground'>Track clicks, referrers, and user behavior as it happens.</p>
						</div>
					</CardContent>
				</Card>
				<Card>
					<CardContent className='pt-6'>
						<div className='flex flex-col items-center space-y-2 text-center'>
							<div className='flex h-12 w-12 items-center justify-center rounded-full bg-primary/10'>
								<Shield className='h-6 w-6 text-primary' />
							</div>
							<h3 className='text-xl font-bold'>Enhanced Security</h3>
							<p className='text-muted-foreground'>Protect your links with password protection and expiration dates.</p>
						</div>
					</CardContent>
				</Card>
			</div>
		</div>
	</section>
)


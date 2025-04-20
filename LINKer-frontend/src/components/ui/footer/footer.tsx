import { Link } from '@tanstack/react-router'
import { GithubIcon, LinkIcon } from 'lucide-react'

export const Footer = () => (
	<footer className='w-full border-t py-6 md:py-0'>
		<div className='container flex flex-col items-center justify-between gap-4 md:h-24 md:flex-row md:ms-20'>
			<div className='flex items-center gap-2 '>
				<LinkIcon className='h-5 w-5 text-primary' />
				<p className='text-sm font-medium'>© {new Date().getFullYear()} LINKer. All rights reserved.</p>
			</div>
			<div className='flex items-center gap-4'>
				<Link to='/about' className='text-sm font-medium hover:text-primary'>
					About
				</Link>
				<div className='flex items-center gap-2'>
					<a
						href='https://github.com/MSB-s-Projects/LINKer'
						target='_blank'
						className='text-muted-foreground hover:text-primary'>
						<GithubIcon className='h-5 w-5' />
						<span className='sr-only'>GitHub</span>
					</a>
				</div>
			</div>
		</div>
	</footer>
)

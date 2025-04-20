import { Link } from '@tanstack/react-router'
import { Menu } from 'lucide-react'
import { Accordion } from '../accordion'
import { Button } from '../button'
import { ModeToggle } from '../mode-toggle'
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from '../sheet'
import { NavbarConfig } from './navbar.config'

export default function Navbar() {
	return (
		<nav className='fixed left-1/2 top-0 z-50 mt-7 flex w-11/12 max-w-7xl -translate-x-1/2 flex-col items-center rounded-full bg-background/20 p-3 backdrop-blur-lg md:rounded-full border-2'>
			<div className='flex w-full items-center justify-between'>
				<Link to={NavbarConfig.brand.href}>
					<Button variant='outline' className='flex items-center gap-2 rounded-2xl'>
						<NavbarConfig.brand.logo className='size-4' />
						<div>{NavbarConfig.brand.name}</div>
					</Button>
				</Link>

				{/* Desktop Menue */}
				<div className='flex items-center gap-4'>
					<div className='hidden'>
						{NavbarConfig.routes.map((item) => (
							<Link key={item.href} to={item.href}>
								{item.title}
							</Link>
						))}
					</div>
				</div>

				<div className='hidden md:block'>
					<div className='flex items-center gap-2'>
						<Button>Login</Button>
						<ModeToggle />
					</div>
				</div>

				{/* Mobile Menue */}
				<div className=' lg:hidden flex items-center justify-between'>
					<Sheet>
						<SheetTrigger asChild>
							<Button variant='outline' size='icon'>
								<Menu className='size-4' />
							</Button>
						</SheetTrigger>
						<SheetContent className='overflow-y-auto'>
							<SheetHeader>
								<SheetTitle>
									<div className='flex items-center w-full'>
										<Link to='/' className='flex items-center gap-2'>
											<div className='flex rounded-full items-center gap-2 p-3 text-foreground transition-colors hover:bg-muted hover:text-accent-foreground'>
												{NavbarConfig.brand.logo && <NavbarConfig.brand.logo className='size-4' />}
											</div>
										</Link>
										<ModeToggle />
									</div>
								</SheetTitle>
							</SheetHeader>
							<div className='flex flex-col gap-6 p-4'>
								<Accordion type='single' collapsible className='flex w-full flex-col gap-4'>
									{NavbarConfig.routes.map((item) => (
										<Link key={item.title} to={item.href} className='text-md font-semibold'>
											{item.title}
										</Link>
									))}
								</Accordion>
								<div className='flex flex-col gap-3'>
									<Button variant='outline'>Login</Button>
								</div>
							</div>
						</SheetContent>
					</Sheet>
				</div>
			</div>
		</nav>
	)
}

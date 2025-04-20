import { Footer } from '@/components/ui/footer/footer'
import Navbar from '@/components/ui/navbar/navbar'
import { Outlet, createRootRoute } from '@tanstack/react-router'
import { TanStackRouterDevtools } from '@tanstack/react-router-devtools'

export const Route = createRootRoute({
	component: RootComponent,
})

function RootComponent() {
	return (
		<>
			<section id='navbar'>
				<Navbar />
			</section>
			<main className='mt-24'>
				<Outlet />
			</main>
			<Footer />
			<TanStackRouterDevtools />
		</>
	)
}


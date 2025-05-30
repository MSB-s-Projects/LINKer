import { LoginForm } from '@/components/login-form'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/auth/login')({
	component: RouteComponent,
})

function RouteComponent() {
	return (
		<div className='flex w-full items-center justify-center p-6 md:p-10 my-24'>
			<div className='w-full max-w-sm'>
				<LoginForm />
			</div>
		</div>
	)
}


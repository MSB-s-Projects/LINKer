import { SignupForm } from '@/components/signup-form'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/auth/signup')({
	component: RouteComponent,
})

function RouteComponent() {
	return (
		<div className='flex w-full items-center justify-center p-6 md:p-5'>
			<div className='w-full max-w-sm'>
				<SignupForm />
			</div>
		</div>
	)
}


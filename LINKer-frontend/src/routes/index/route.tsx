import { H1 } from '@/components/ui/typography'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/')({
	component: RouteComponent,
})

function RouteComponent() {
	return <H1>Hello World!</H1>
}


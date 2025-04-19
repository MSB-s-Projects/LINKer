import { H1 } from '@/components/ui/h1'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/')({
  component: App,
})

function App() {
	return (
		<>
			<H1>Hello World!</H1>
		</>
	)
}

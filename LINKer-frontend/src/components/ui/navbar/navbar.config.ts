import { Link } from 'lucide-react'

export const NavbarConfig = {
	brand: {
		logo: Link,
		name: 'LINKer',
		href: '/',
	},
	routes: [
		{
			title: 'About',
			href: '/about',
		},
		// {
		// 	title: 'Features',
		// 	href: '/features',
		// 	items: [
		// 		{
		// 			title: 'Feature 1',
		// 			href: '/features/feature1',
		// 		},
		// 		{
		// 			title: 'Feature 2',
		// 			href: '/features/feature2',
		// 		},
		// 	],
		// },
	],
}

export type NavbarConfigType = typeof NavbarConfig

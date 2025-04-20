import { Button } from '@/components/ui/button'
import { P } from '@/components/ui/typography'

export const Hero = () => (
	<section id='hero'>
		<div className='lg:flex-row flex-col lg:gap-10 gap-8 flex justify-between items-center m-4 mb-20'>
			<div className='flex-1'>
				<P className='mb-6 leading-relaxed'>
					LINKer streamlines the process of URL shortening, making sharing links effortless and efficient. With its
					user-friendly interface, LINKer allows you to generate concise, easy-to-share URLs in seconds. Simplify your
					sharing experience with LINKer today.
				</P>
				<div className='flex items-center gap-3 mb-10'>
					<Button variant='default'>Manage Links</Button>
					<Button variant='outline'>Create Short Link</Button>
				</div>
			</div>
			<div className='flex items-center justify-center'>
				<img src='/logo.png' alt='logo' className='sm:w-[460px] w-[400px] rounded-lg object-cover' />
			</div>
		</div>
	</section>
)

import React from "react"

function H1({ ...props }: React.ComponentProps<"h1">) {
	return <h1 className='scroll-m-20 text-4xl font-extrabold tracking-tight lg:text-5xl'>{props.children}</h1>
}

export { H1 }

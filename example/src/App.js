import * as React from 'react';
import { useSafeAreaInsetsState } from './safe-area-context';
const App = () => {
	const [alignment, setAlignment] = React.useState("left");
	const safeAreaInsets = useSafeAreaInsetsState();

	return (
		<div
			style={{
				display: "flex",
				flexDirection: "column",
				height: "100vh",
				backgroundColor: "gray"
			}}>
			<div
				style={{
					height: "var(--safe-area-inset-top)",
					backgroundColor: "#12005e"
				}}/>
			<div
				style={{
					height: "100%",
					backgroundColor: "#f5f5f5"
				}}>
				<header
					style={{
						backgroundColor: "#4a148c",
						color: "white",
						padding: "1.0rem max(1.5rem, var(--safe-area-inset-right)) 1.0rem max(1.5rem, var(--safe-area-inset-left))",
						fontSize: "1.75rem",
						textAlign: `${alignment}`
					}}>
					Text
				</header>
				<div
					style={{
						height: "150px",
						backgroundColor: "#e0e0e0",
						position: "relative"
					}}>
					<span
						style={{
							position: "absolute",
							width: "100%",
							top: "50%",
							textAlign: "center"
						}}>
						{
							JSON.stringify(safeAreaInsets)
						}
					</span>
				</div>
				<section
					style={{
						padding: "1.0rem max(1.5rem, var(--safe-area-inset-left)) 1.5rem max(1.5rem, var(--safe-area-inset-left))"
					}}>
					<h2
						style={{
							fontWeight: "normal",
							fontSize: "1.5rem",
							textAlign: "center"
						}}>Header Alignment</h2>
					<div
						style={{
							display: "flex",
							justifyContent: "center",
							paddingTop: "2.5rem"
						}}>
						<button
							style={{
								padding: "0.5rem 1.0rem 0.5rem 1.0rem",
								backgroundColor: "#1976d2",
								border: "0px",
								outline: "none",
								color: "#FFFFFF",
								fontSize: "1.0rem"
							}}
							onClick={() => setAlignment("left")}>Left</button>
						<button
							style={{
								padding: "0.5rem 1.0rem 0.5rem 1.0rem",
								backgroundColor: "#1976d2",
								border: "0px",
								outline: "none",
								color: "#FFFFFF",
								fontSize: "1.0rem",
								marginLeft: "0.5rem"
							}}
							onClick={() => setAlignment("center")}>Center</button>
						<button
							style={{
								padding: "0.5rem 1.0rem 0.5rem 1.0rem",
								backgroundColor: "#1976d2",
								border: "0px",
								outline: "none",
								color: "#FFFFFF",
								fontSize: "1.0rem",
								marginLeft: "0.5rem"
							}}
							onClick={() => setAlignment("right")}>Right</button>
					</div>
				</section>
			</div>
		</div>
	);
}

export default App;

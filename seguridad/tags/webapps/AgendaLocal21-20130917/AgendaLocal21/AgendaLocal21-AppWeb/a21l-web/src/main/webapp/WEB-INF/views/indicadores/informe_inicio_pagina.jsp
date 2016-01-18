		<c:set var="numero_pagina" value="${numero_pagina + 1}" />
		
		<div class="pagina a4">
			<table class="tabla-a4" cellpadding="0" cellspacing="0">
				<thead> 
					<tr>
						<td>
							<div id="cabecera">
								<div class="conjuntocabecerapdf">
									<div class="logoscomplementos"> <img src="images/loured.png" alt="Loured" width="114" height="33" /> <img src="images/planavanza.png" alt="Plan Avanza2" width="169" height="33" /> </div>
									<h1><a href="indicadores.htm"><img src="images/logo.png" alt="Agenda Local 21" width="232" height="80" /></a></h1>
								</div>
							</div>
						</td>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<td>
							<div class="pie-aviso-legal">Aviso Legal &copy; Agenda Local 21. Información mantenida y publicada por Proyecto LOURED.</div>
							<div class="numero-pagina">P&aacute;gina ${numero_pagina} / ${numero_paginas_total}</div>
						</td>
					</tr>
				</tfoot>
				<tbody>
					<tr>
						<td>
# This script tests the star size commands.
# Created by Toni Sagrista

from py4j.java_gateway import JavaGateway, GatewayParameters

gateway = JavaGateway(gateway_parameters=GatewayParameters(auto_convert=True))
gs = gateway.entry_point

gs.maximizeInterfaceWindow()

gs.setStarSize(100.0)
gs.sleep(2)
gs.setStarSize(70.0)
gs.sleep(2)
gs.setStarSize(50.0)
gs.sleep(2)
gs.setStarSize(30.0)
gs.sleep(2)
gs.setStarSize(12.0)
gs.sleep(2)

gateway.close()
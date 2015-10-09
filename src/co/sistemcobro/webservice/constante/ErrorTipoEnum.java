package co.sistemcobro.webservice.constante;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ErrorTipoEnum {
	CORRECTO(0),ERROR_USUARIO_OR_PASS(1),ERROR_USUARIO_NO_ENCONTRADO(2),ERROR_VALIDANDO_USUARIO(3);

	public static final Map<Integer, ErrorTipoEnum> mapByID = new HashMap<Integer, ErrorTipoEnum>();
	public static final Map<String, ErrorTipoEnum> mapByNAME = new HashMap<String, ErrorTipoEnum>();

	static {
		for (ErrorTipoEnum s : EnumSet
				.allOf(ErrorTipoEnum.class)) {
			mapByID.put(s.getIndex(), s);
			mapByNAME.put(s.name(), s);
		}
	}

	private int index;

	ErrorTipoEnum(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public String getIndexString() {
		return String.valueOf(index);
	}

	public static ErrorTipoEnum get(int id) {
		return mapByID.get(id);
	}

	public static ErrorTipoEnum get(String name) {
		return mapByNAME.get(name);
	}

}
